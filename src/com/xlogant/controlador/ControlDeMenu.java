/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.xlogant.acciones.accionaFormulario;
import com.xlogant.modelo.menu.MenuItemDTO;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Construye dinámicamente la barra de menú para un usuario específico
 * basándose en sus permisos.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public class ControlDeMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = -7647460907357980635L;
    private static final Logger LOGGER = Logger.getLogger(ControlDeMenu.class.getName());

    private final MenuService menuService;

    public ControlDeMenu() {
        this.menuService = new MenuService();
    }

    /**
     * Carga los menús para un usuario y los añade a la barra de menú proporcionada.
     *
     * @param menuBar     La barra de menú a la que se añadirán los menús.
     * @param desktopPane El panel de escritorio para la acción de los submenús.
     * @param userId      El ID del usuario.
     * @param roleId      El ID del rol del usuario.
     */
    public void buildMenuForUser(JMenuBar menuBar, JDesktopPane desktopPane, long userId, long roleId) {
        try {
            List<MenuItemDTO> menuItems = menuService.getMenuItemsForUser(userId, roleId);

            for (MenuItemDTO menuDTO : menuItems) {
                JMenu menu = new JMenu(menuDTO.name());
                menuBar.add(menu);

                for (var subMenuDTO : menuDTO.subItems()) {
                    JMenuItem subMenuItem = new JMenuItem(subMenuDTO.name());
                    if (subMenuDTO.enabled()) {
                        subMenuItem.addActionListener(new accionaFormulario(desktopPane));
                    } else {
                        subMenuItem.setEnabled(false);
                    }
                    menu.add(subMenuItem);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al construir el menú para el usuario ID: " + userId, e);
            JOptionPane.showMessageDialog(
                    desktopPane,
                    "No se pudo cargar el menú de usuario.\nError: " + e.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
