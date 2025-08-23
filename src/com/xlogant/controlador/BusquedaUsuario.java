/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.xlogant.modelo.UsuarioDB;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;

import static com.xlogant.conecta.ConectaDB.cerrarConexion;
import static com.xlogant.conecta.ConectaDB.obtenerConexion;

/**
 * @author oscar
 */
public class BusquedaUsuario implements Serializable {

    public BusquedaUsuario(JMenuItem unItem) {
        try {
            conexion = obtenerConexion();
            pstm = conexion.prepareStatement(CONSULTALL);
            pst = conexion.prepareStatement(CONSULTA);
        } catch (SQLException ex) {
            Logger.getLogger(BusquedaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getCause());
        }
    }

    public List<UsuarioDB> getDatosUsuario() {
        var datosUsuario = new LinkedList<UsuarioDB>();
        try {
            try (var losresuktados = pstm.executeQuery()) {
                while (losresuktados.next()) {
                    datosUsuario.add(new UsuarioDB(losresuktados.getLong(1),
                            losresuktados.getString(2), losresuktados.getString(3),
                            losresuktados.getBoolean(4), losresuktados.getInt(5)));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        } finally {
            cerrarConexion(conexion);
        }
        return datosUsuario;
    }

    public List<UsuarioDB> obtenerId(String nombre, String clave, boolean activo) {
        var LosUsuarios = new LinkedList<UsuarioDB>();
        try {
            pst.setString(1, nombre);
            pst.setString(2, clave);
            pst.setBoolean(3, activo);
            try (var resulta = pst.executeQuery()) {
                while (resulta.next()) {
                    LosUsuarios.add(new UsuarioDB(resulta.getInt(1),
                            resulta.getString(2), resulta.getString(3),
                            resulta.getBoolean(4), resulta.getInt(5)));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        } finally {
            cerrarConexion(conexion);
        }
        return LosUsuarios;
    }


    private Connection conexion = null;
    private PreparedStatement pstm = null;
    private PreparedStatement pst;
    private ResultSet resultado = null;
    private final static String CONSULTALL = "SELECT * FROM datos_usuario";
    private final static String CONSULTA = "SELECT id_role FROM datos_usuario "
            + "WHERE usuario_nombre= ? AND clave_usuario= ? "
            + "AND estatus_de_usuario= ?";
    @Serial
    private static final long serialVersionUID = -3448797666703898568L;

}
