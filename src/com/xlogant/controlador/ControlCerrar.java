/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.xlogant.conecta.ConectaDB;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de servicio para registrar eventos de cierre de sesión de usuario.
 *
 * @author oscar
 */
public final class ControlCerrar implements Serializable {

    @Serial
    private static final long serialVersionUID = 5986634552120736445L;
    private static final Logger LOGGER = Logger.getLogger(ControlCerrar.class.getName());
    private static final String CERRANDO = "SALIDA";
    private static final String INSERTANDO = "INSERT INTO ctrol_cerrar(id_usuario, id_role, hora_cerrado, describe) VALUES(?, ?, ?, ?)";

    /**
     * Registra un evento de salida para un usuario en la base de datos.
     * <p>
     * Esta operación se ejecuta dentro de una transacción. Si la inserción falla,
     * la transacción se revierte.
     *
     * @param userId el ID del usuario que cierra sesión.
     * @param roleId el ID del rol del usuario.
     * @throws SQLException si ocurre un error de comunicación con la base de datos
     *                      o si la inserción no afecta a exactamente una fila.
     */
    public static void registraSalida(long userId, long roleId) throws SQLException {
        // try-with-resources para la conexión, asegurando su cierre.
        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción.
            // try-with-resources para el PreparedStatement.
            try (PreparedStatement pstmt = conn.prepareStatement(INSERTANDO)) {
                pstmt.setLong(1, userId);
                pstmt.setLong(2, roleId);
                pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setString(4, CERRANDO);
                pstmt.executeUpdate();
                conn.commit(); // Finalizar transacción con éxito.
                LOGGER.log(Level.INFO, "Salida registrada para el usuario ID: {0}", userId);
            } catch (SQLException e) {
                conn.rollback(); // Revertir transacción en caso de error.
                LOGGER.log(Level.SEVERE, "Fallo al registrar salida para el usuario ID: " + userId, e);
                throw e; // Relanzar la excepción para que el llamador sepa que falló.
            }
        }
    }
}
