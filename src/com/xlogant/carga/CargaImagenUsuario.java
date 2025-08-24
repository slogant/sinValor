/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.carga;

import com.xlogant.conecta.ConectaDB;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para cargar imágenes de usuario desde la base de datos.
 * Esta clase está diseñada para ser instanciada y no utiliza estado estático,
 * lo que la hace segura para su uso en entornos con múltiples hilos.
 *
 * @author oscar
 */
public final class CargaImagenUsuario implements Serializable {

    @Serial
    private static final long serialVersionUID = -1257200097624081967L;
    private static final Logger LOGGER = Logger.getLogger(CargaImagenUsuario.class.getName());

    private static final String FETCH_IMAGE_QUERY = "SELECT foto_usuario FROM datos_usuarios du "
            + "INNER JOIN datos_usuario dus ON du.id_ctrol_usuario = dus.id_dato_control "
            + "WHERE du.id_ctrol_usuario = ? AND dus.usuario_nombre = ?";

    /**
     * Carga la imagen de un usuario desde la base de datos como un BufferedImage.
     *
     * @param userId El ID de control del usuario.
     * @param userName El nombre del usuario.
     * @return Un {@link Optional} que contiene la {@link BufferedImage} si se encuentra,
     *         o un Optional vacío si no se encuentra o si ocurre un error.
     */
    public Optional<BufferedImage> fetchImageAsBufferedImage(int userId, String userName) {
        // Utiliza try-with-resources para garantizar el cierre de todos los recursos.
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FETCH_IMAGE_QUERY)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, userName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Usa otro try-with-resources para el InputStream del blob.
                    try (InputStream binaryStream = rs.getBinaryStream("foto_usuario")) {
                        if (binaryStream != null) {
                            // ImageIO.read puede lanzar IOException.
                            return Optional.ofNullable(ImageIO.read(binaryStream));
                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Fallo al cargar la imagen para el usuario ID: " + userId, ex);
        }
        return Optional.empty();
    }

    /**
     * Carga la imagen de un usuario desde la base de datos como un ImageIcon.
     *
     * @param userId El ID de control del usuario.
     * @param userName El nombre del usuario.
     * @return Un {@link Optional} que contiene el {@link ImageIcon} si se encuentra,
     *         o un Optional vacío en caso contrario.
     */
    public Optional<ImageIcon> fetchImageAsIcon(int userId, String userName) {
        return fetchImageAsBufferedImage(userId, userName).map(ImageIcon::new);
    }
}
