package com.xlogant.acciones;

import com.xlogant.conecta.ConectaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para validar un RFID contra la base de datos.
 */
public class RfidValidatorService {

    private static final Logger LOGGER = Logger.getLogger(RfidValidatorService.class.getName());
    private static final String RFID_EXISTS_QUERY = "SELECT 1 FROM datos_usuarios WHERE rfid = ?";

    /**
     * Verifica si un RFID ya existe en la base de datos.
     *
     * @param rfid El RFID a verificar.
     * @return true si el RFID existe, false en caso contrario o si ocurre un error.
     */
    public boolean rfidExists(String rfid) {
        if (rfid == null || rfid.isBlank()) {
            return false;
        }

        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RFID_EXISTS_QUERY)) {
            pstmt.setString(1, rfid);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, el RFID existe.
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al validar el RFID: " + rfid, e);
            return false; // En caso de error, asumimos que no es válido para evitar duplicados.
        }
    }
}