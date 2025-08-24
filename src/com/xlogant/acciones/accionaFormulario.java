/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Un ActionListener que carga y muestra dinámicamente un JInternalFrame
 * dentro de un JDesktopPane basado en una búsqueda en la base de datos.
 * Previene la creación de formularios duplicados y realiza las operaciones
 * de base de datos fuera del Hilo de Despacho de Eventos (EDT) para mantener
 * la UI responsiva.
 *
 * @author oscar
 */
public class accionaFormulario implements ActionListener, Serializable {

    @Serial
    private static final long serialVersionUID = -2570927168571265260L;
    private static final Logger LOGGER = Logger.getLogger(accionaFormulario.class.getName());

    private final JDesktopPane desktopPane;
    private final FormLoaderService formLoader;

    public accionaFormulario(JDesktopPane desktop) {
        if (desktop == null) {
            throw new IllegalArgumentException("El JDesktopPane no puede ser nulo.");
        }
        this.desktopPane = desktop;
        this.formLoader = new FormLoaderService();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof JMenuItem menuItem)) {
            return; // No es un JMenuItem, no hacer nada.
        }

        String formName = e.getActionCommand();
        if (formName == null || formName.isBlank()) {
            LOGGER.warning("El comando de acción del menú está vacío.");
            return;
        }

        // Deshabilitar el menú para prevenir dobles clics mientras se carga.
        menuItem.setEnabled(false);

        // Usar SwingWorker para realizar la búsqueda en la BD fuera del EDT.
        new SwingWorker<Optional<String>, Void>() {
            @Override
            protected Optional<String> doInBackground() {
                // La única operación lenta (acceso a BD) se hace aquí.
                return formLoader.getFormClassName(formName);
            }

            @Override
            protected void done() {
                try {
                    Optional<String> classNameOpt = get();

                    if (classNameOpt.isEmpty() || classNameOpt.get().isBlank()) {
                        showError("No se pudo encontrar la configuración para el formulario: " + formName);
                        return;
                    }

                    String className = classNameOpt.get();

                    // --- Toda la lógica de UI se ejecuta ahora en el EDT ---

                    // 1. Verificar si ya existe una instancia de este frame.
                    for (JInternalFrame frame : desktopPane.getAllFrames()) {
                        if (frame.getClass().getName().equals(className)) {
                            // Si el frame fue cerrado, lo removemos para crear uno nuevo.
                            if (frame.isClosed()) {
                                desktopPane.remove(frame);
                                break; // Salir del bucle para proceder a la creación.
                            }

                            // Si ya existe y está abierto, lo traemos al frente.
                            if (frame.isIcon()) {
                                desktopPane.getDesktopManager().deiconifyFrame(frame);
                            }
                            frame.moveToFront();
                            frame.setSelected(true);
                            return; // Tarea completada.
                        }
                    }

                    // 2. Si no se encontró un frame abierto, crear uno nuevo.
                    Class<?> frameClass = Class.forName(className);
                    JInternalFrame newFrame = (JInternalFrame) frameClass.getConstructor().newInstance();

                    // 3. Añadir y mostrar el nuevo frame.
                    desktopPane.add(newFrame);
                    centerFrame(newFrame);
                    newFrame.setVisible(true);
                    newFrame.moveToFront();
                    newFrame.setSelected(true);

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Fallo al cargar o mostrar el formulario: " + formName, ex);
                    // Usar ex.getCause() puede dar un mensaje más específico.
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    showError("Error al cargar el formulario: " + cause.getMessage());
                } finally {
                    // Siempre volver a habilitar el menú.
                    menuItem.setEnabled(true);
                }
            }

            private void showError(String message) {
                JOptionPane.showMessageDialog(
                        desktopPane,
                        message,
                        "Error de Carga",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            private void centerFrame(JInternalFrame frame) {
                Dimension desktopSize = desktopPane.getSize();
                Dimension frameSize = frame.getSize();
                int x = (desktopSize.width - frameSize.width) / 2;
                int y = (desktopSize.height - frameSize.height) / 2;
                // Asegurarse de que el frame no se posicione fuera de la pantalla si es muy grande.
                x = Math.max(0, x);
                y = Math.max(0, y);
                frame.setLocation(new Point(x, y));
            }

        }.execute();
    }
}
