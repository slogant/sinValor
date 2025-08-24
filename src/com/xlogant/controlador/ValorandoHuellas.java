/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.xlogant.conecta.ConectaDB;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de servicio (DAO) para cargar las plantillas de huellas dactilares
 * de un usuario desde la base de datos.
 * <p>
 * Esta clase es sin estado y utiliza un método estático para realizar la consulta.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
final public class ValorandoHuellas implements Serializable {

    @Serial
    private static final long serialVersionUID = -4643084159888177855L;
    private static final Logger LOGGER = Logger.getLogger(ValorandoHuellas.class.getName());
    private final static String CONSULTAHUELLA = "SELECT ch.id_usuario,ch.identificador_huella, ch.huella_reg FROM control_huellas ch WHERE ch.id_usuario= ?";

    /**
     * Obtiene todas las plantillas de huellas dactilares asociadas a un ID de usuario.
     *
     * @param userId el ID del usuario cuyas huellas se van a recuperar.
     * @return un {@link EnumMap} que mapea el índice del dedo a su plantilla de huella.
     * @throws SQLException si ocurre un error de comunicación con la base de datos.
     */
    public static EnumMap<DPFPFingerIndex, DPFPTemplate> obtieneHuellas(int userId) throws SQLException {
        EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<>(DPFPFingerIndex.class);

        // try-with-resources asegura que la conexión y otros recursos se cierren siempre.
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CONSULTAHUELLA)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String fingerIndexName = rs.getString("identificador_huella");
                    byte[] templateBuffer = rs.getBytes("huella_reg");

                    DPFPFingerIndex fingerIndex = DPFPFingerIndex.valueOf(fingerIndexName);
                    DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);

                    templates.put(fingerIndex, template);
                }
            }
        }
        LOGGER.log(Level.INFO, "Se encontraron {0} huellas para el usuario ID: {1}", new Object[]{templates.size(), userId});
        return templates;
    }
}
