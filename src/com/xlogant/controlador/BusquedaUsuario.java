/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import java.io.Serial;
import java.io.Serializable;
import com.xlogant.conecta.ConectaDB;
import com.xlogant.modelo.UsuarioDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) para la entidad Usuario.
 * Proporciona métodos para consultar usuarios en la base de datos.
 * <p>
 * Esta clase es un servicio sin estado; es seguro crear una instancia
 * y llamar a sus métodos.
 *
 * @author oscar
 */
public class BusquedaUsuario implements Serializable {

    @Serial
    private static final long serialVersionUID = -3448797666703898568L;

    private static final String FIND_ALL_QUERY = "SELECT * FROM datos_usuario";
    // Se corrigió la consulta para seleccionar todas las columnas necesarias.
    private static final String FIND_BY_CREDENTIALS_QUERY = "SELECT * FROM datos_usuario "
            + "WHERE usuario_nombre = ? AND clave_usuario = ? AND status_de_usuario = ?";

    /**
     * Obtiene una lista de todos los usuarios de la base de datos.
     *
     * @return una lista de objetos UsuarioDB.
     * @throws SQLException si ocurre un error durante el acceso a la base de datos.
     */
    public List<UsuarioDB> getDatosUsuario() throws SQLException {
        List<UsuarioDB> usuarios = new LinkedList<>();
        // try-with-resources asegura que la conexión y otros recursos se cierren siempre.
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_ALL_QUERY);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    /**
     * Obtiene una lista de usuarios que coinciden con las credenciales proporcionadas.
     *
     * @param nombre el nombre de usuario.
     * @param clave  la clave del usuario.
     * @param activo el estado de actividad del usuario.
     * @return una lista de objetos UsuarioDB que coinciden.
     * @throws SQLException si ocurre un error durante el acceso a la base de datos.
     */
    public List<UsuarioDB> obtenerId(String nombre, String clave, boolean activo) throws SQLException {
        List<UsuarioDB> usuarios = new LinkedList<>();
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_CREDENTIALS_QUERY)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, clave);
            pstmt.setBoolean(3, activo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
        }
        return usuarios;
    }

    /**
     * Mapea la fila actual de un ResultSet a un objeto UsuarioDB.
     */
    private UsuarioDB mapResultSetToUsuario(ResultSet rs) throws SQLException {
        // Asumiendo que el constructor de UsuarioDB es (long, String, String, boolean, int)
        // y que las columnas están en ese orden.
        return new UsuarioDB(
                rs.getLong(1),
                rs.getString(2),
                rs.getString(3),
                rs.getBoolean(4),
                rs.getInt(5)
        );
    }
}
