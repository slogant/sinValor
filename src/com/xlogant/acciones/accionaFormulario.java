/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

import com.xlogant.conecta.ConectaDB;
import com.xlogant.controlador.ControlDeMenu;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import static java.awt.Toolkit.*;
import static java.lang.Class.forName;
import static java.lang.System.out;
import static javax.swing.JOptionPane.*;

/**
 *
 * @author oscar
 */
 public class accionaFormulario implements ActionListener, Serializable {

    public accionaFormulario(JDesktopPane desktop) {
        paneDesktop = desktop;
    }

    @Override public void actionPerformed(ActionEvent e) {
        try {
            var sender = e.getActionCommand();
            var obj = e.getSource();
            if (obj instanceof JMenuItem menux) {
                showInternalMessageDialog(this.paneDesktop, sender, "Aqui", INFORMATION_MESSAGE);
                elItem = menux;
                conectaItem = ConectaDB.obtenerConexion();
                pidmenu = conectaItem.prepareStatement(CONSULTAFORMULARIO);
                pidmenu.setString(1, sender);
                rmnuid = pidmenu.executeQuery();
                while (rmnuid.next()) {
                    var descripcion = rmnuid.getString(1);
                    var url_submenus = rmnuid.getString(2);
                    var clase = forName(url_submenus);
                    var claseCarga = clase.getSimpleName();
                    var userConstructor = clase.getConstructor();
                    if (d == null) {
                        elItem.setEnabled(false);
                        d = (JInternalFrame) userConstructor.newInstance();
                        var pantalla = getDefaultToolkit().getScreenSize();
                        var ventana = d.getSize();
                        var x = ((pantalla.width - ventana.width) / 2);
                        var y = ((pantalla.height - ventana.height) / 2);
                        d.setLocation(new Point(x, y));
                        d.setVisible(true);
                        d.moveToFront();
                        d.toFront();
                        d.getFocusOwner();
                        d.repaint();
                        d.revalidate();
                        paneDesktop.add(d);
                    } else if (d != null) {
                        d = null;
                        if (pidmenu != null) {
                            ConectaDB.cerrarPreparaStatement(pidmenu);
                        }
                        if (rmnuid != null) {
                            ConectaDB.cerrarResultSet(rmnuid);
                        }
                        if (conectaItem != null) {
                            ConectaDB.cerrarConexion(conectaItem);
                        }
                        elItem.setEnabled(false);
                        if (d == null) {
                            elItem.setEnabled(false);
                            var g = (d == null);
                            out.println("Cerrado correctamente " + g);
                            d = (JInternalFrame) userConstructor.newInstance();
                            paneDesktop.add(d);
                            var pantalla = getDefaultToolkit().getScreenSize();
                            var ventana = d.getSize();
                            var x = ((pantalla.width - ventana.width) / 2);
                            var y = ((pantalla.height - ventana.height) / 2);
                            d.moveToFront();
                            d.toFront();
                            d.getFocusOwner();
                            d.setLocation(new Point(x, y));
                            d.setVisible(true);
                            d.revalidate();
                            d.repaint();
                            if (pidmenu != null) {
                                ConectaDB.cerrarPreparaStatement(pidmenu);
                            }
                            if (rmnuid != null) {
                                ConectaDB.cerrarResultSet(rmnuid);
                            }
                            if (conectaItem != null) {
                                ConectaDB.cerrarConexion(conectaItem);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ControlDeMenu.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex.getCause());
            out.println(ex.getLocalizedMessage());
        } finally {
            if (pidmenu != null) {
                ConectaDB.cerrarPreparaStatement(pidmenu);
            }
            if (rmnuid != null) {
                ConectaDB.cerrarResultSet(rmnuid);
            }
            if (conectaItem != null) {
                ConectaDB.cerrarConexion(conectaItem);
            }
        }
    }

    @Serial
    static final private long serialVersionUID = -2570927168571265260L;
    final private JDesktopPane paneDesktop;
    private JInternalFrame d;
    static public JMenuItem elItem;
    static private Connection conectaItem;
    static private PreparedStatement pidmenu;
    static private ResultSet rmnuid;
    static final private String CONSULTAFORMULARIO = "SELECT descripcion_submenu, url_submenu "
            + "FROM submenu_principal  "
            + "WHERE nombre_submenu= ?";

}
