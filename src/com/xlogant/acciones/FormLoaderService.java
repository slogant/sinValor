package com.xlogant.acciones;

import com.xlogant.conecta.ConectaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para cargar información de formularios desde la base de datos.
 */
public class FormLoaderService {

    private static final Logger LOGGER = Logger.getLogger(FormLoaderService.class.getName());
    private static final String FORM_QUERY = "SELECT url_submenu FROM submenu_principal WHERE nombre_submenu = ?";

    /**
     * Obtiene el nombre de la clase de un formulario a partir de su nombre de menú.
     * @param formName El nombre del formulario (del menú).
     * @return Un Optional con el nombre de la clase, o vacío si no se encuentra o hay un error.
     */
    public Optional<String> getFormClassName(String formName) {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FORM_QUERY)) {
            pstmt.setString(1, formName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.ofNullable(rs.getString("url_submenu")) : Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al consultar el nombre de la clase para el formulario: " + formName, e);
            return Optional.empty();
        }
    }
}