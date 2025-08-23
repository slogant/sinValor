/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.conecta;

import com.xlogant.principal.CentroPrincipal;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.lang.System.*;
import static java.sql.DriverManager.getConnection;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showInternalMessageDialog;

/**
 *
 * @author oscar
 */
final public class ConectaDB implements Serializable {

    static public Connection obtenerConexion() {
        try {
            conexion = getConnection(URLDB, USUARIO, CLAVE);
            conexion.setAutoCommit(false);
            out.println("Conexion abierta");
        } catch (SQLException e) {
            out.println("Error: -> " + e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
            exit(0);
        }
        return conexion;
    }

    static public void cerrarConexion(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            out.println(e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
        }
    }

    static public void cerrarPreparaStatement(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            out.println(e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
        }
    }

    static public void cerrarResultSet(ResultSet re) {
        try {
            if (re != null) {
                re.close();
            }
        } catch (SQLException e) {
            out.println(e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
        }
    }

    static public void cerrarCommit(Connection con) {
        try {
            if (con != null) {
                con.commit();
            }
        } catch (SQLException e) {
            out.println(e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
        }
    }

    static public void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            out.println(e.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", WARNING_MESSAGE);
        }
    }
    
    @Serial
    static final private long serialVersionUID = -6300264245168250633L;
    static final private String URLDB = "jdbc:postgresql://localhost:5432/centro";
    //static final private String URLDB = "jdbc:postgresql://centro.cuesfczh4pdj.us-east-2.rds.amazonaws.com:5432/centro";
    static final private String USUARIO = "oscar", CLAVE = "marieljava";
    static private Connection conexion;
}
