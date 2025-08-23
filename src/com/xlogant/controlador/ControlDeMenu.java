/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.xlogant.acciones.accionaFormulario;
import com.xlogant.conecta.ConectaDB;

import java.io.Serial;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import static com.xlogant.conecta.ConectaDB.*;

/**
 *
 * @author oscar
 */
public class ControlDeMenu implements Serializable {

    public ControlDeMenu() {
    }

    public static void obtenerMenus(long id_usuario, Long roles, boolean act, JDesktopPane pane,
            JMenuBar unaBarra, JMenu unMenu, JMenuItem submenus) {
        String menus, nombreSubmenu, nombreMenu;
        boolean controlActivo, controlActivoSub;
        long idMenu;
        long roless, ids;
        long valores = roles;
        JMenu menu = unMenu;
        JMenuItem subItems = submenus;
        var conectados = obtenerConexion();
        var conectadosdb = obtenerConexion();
        try (conectados; conectadosdb;
            var pstm = conectados.prepareStatement(CONSULTAMENU);
            var ps = conectadosdb.prepareStatement(MICONSULTASUBMENUS);) {
            pstm.setLong(1, id_usuario);
            pstm.setLong(2, valores);
            pstm.setBoolean(3, act);
            ResultSet resultado = pstm.executeQuery();
            while (resultado.next()) {
                menus = resultado.getString(1);
                controlActivo = resultado.getBoolean(2);
                idMenu = resultado.getLong(3);
                menu = new JMenu(menus);
                System.out.println(menus);
                unaBarra.add(menu);
                ps.setLong(1, id_usuario);
                ps.setLong(2, valores);
                ps.setLong(3, idMenu);
                ps.setBoolean(4, ACTIVIDAD);
                ps.setBoolean(5, ACTIVIDAD);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    ids = res.getLong(1);
                    roless = res.getLong(2);
                    nombreMenu = res.getString(3);
                    nombreSubmenu = res.getString(4);
                    controlActivoSub = res.getBoolean(5);
                    if (controlActivoSub) {
                        subItems = new JMenuItem(nombreSubmenu);
                        subItems.addActionListener(new accionaFormulario(pane));
                        unaBarra.add(menu);
                        menu.add(subItems);
                        menu.addSeparator();
                    } else {
                        subItems = new JMenuItem(nombreSubmenu);
                        subItems.setEnabled(false);
                        unaBarra.add(menu);
                        menu.add(subItems);
                        menu.addSeparator();
                    }
                }
            }
        } catch (SQLException e) {
            for (var v : e) {
                System.out.println(v.getMessage());
                System.out.println(v.getLocalizedMessage());
            }
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getCause());
        }
        /*finally {
            if (pstm != null) {
                cerrarPreparaStatement(pstm);
            }
            if (ps != null) {
                cerrarPreparaStatement(ps);
            }
            if (resultado != null) {
                cerrarResultSet(resultado);
            }
            if (res != null) {
                cerrarResultSet(res);

                if (conectados != null) {
                    cerrarConexion(conectados);
                }
                if (conectadosdb != null) {
                    cerrarConexion(conectadosdb);
                }
            }
        }*/
    }

    @Serial
    private static final long serialVersionUID = -7647460907357980635L;
    private static Connection conectados, conectadosdb;
    private static PreparedStatement pstm;
    private static PreparedStatement ps;
    private final static boolean ACTIVIDAD = true;

    private final static String CONSULTAMENU = "SELECT DISTINCT mp.nombre_menu, cm.estatus_menu, cm.id_menu FROM control_menu cm "
            + "INNER JOIN menu_principal mp "
            + "ON mp.id_menu = cm.id_menu "
            + "WHERE cm.id_usuario= ? AND cm.id_role = ? AND cm.estatus_menu= ? "
            + "ORDER BY mp.nombre_menu ASC";

    private final static String CONSULTASUBMENU = "SELECT  cm.id_menu, "
            + "cm.id_role, "
            + "mp.nombre_menu, "
            + "sm.nombre_submenu, "
            + "cs.estatus_submenus, "
            + "sm.descripcion_submenu "
            + "FROM control_menu cm "
            + "INNER JOIN menu_principal mp "
            + "ON cm.id_menu = mp.id_menu "
            + "INNER JOIN control_submenu cs "
            + "ON cs.id_menu = mp.id_menu "
            + "INNER JOIN submenu_principal sm "
            + "ON sm.id_submenu = cs.id_submenu "
            + "WHERE cm.id_role = ? "
            + "AND cm.estatus_menu = ? "
            + "AND mp.id_menu= ? "
            + "AND cs.id_usuario = ? "
            + "AND cs.estatus_submenus = ? "
            + "ORDER BY sm.id_submenu ASC";

    private final static String MICONSULTASUBMENUS = "SELECT DISTINCT cm.id_menu,cm.id_role,"
            + "mp.nombre_menu, sp.nombre_submenu,"
            + "csm.estatus_submenus "
            + "FROM menu_principal mp "
            + "INNER JOIN control_menu cm "
            + "ON cm.id_menu=mp.id_menu "
            + "INNER JOIN control_submenu csm "
            + "ON csm.id_menu=mp.id_menu "
            + "INNER JOIN submenu_principal sp "
            + "ON sp.id_submenu = csm.id_submenu "
            + "WHERE cm.id_usuario = ? "
            + "AND cm.id_role = ? "
            + "AND cm.id_menu = ? "
            + "AND cm.estatus_menu = ? "
            + "AND csm.estatus_submenus = ?";

    private final static String MENUID = "SELECT cm.id_menu FROM control_menu cm "
            + "INNER JOIN menu_principal mp "
            + "ON mp.id_menu = cm.id_menu "
            + "WHERE cm.id_role= ? AND cm.estatus_menu= ?"
            + "ORDER BY cm.id_menu ASC";

}
