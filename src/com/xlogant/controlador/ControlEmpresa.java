package com.xlogant.controlador;


import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import com.xlogant.conecta.ConectaDB;

/**
 * Clase de servicio (DAO) para gestionar las entidades Empresa y Representante.
 * <p>
 * Esta clase es sin estado y utiliza métodos estáticos para interactuar con la base de datos.
 * Todas las operaciones de base de datos gestionan sus propios recursos de forma segura
 * y propagan las excepciones SQL para que sean manejadas por la capa de llamada.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public final class ControlEmpresa implements Serializable {

    @Serial
    private static final long serialVersionUID = -8630890133649335687L;
    private static final Logger LOGGER = Logger.getLogger(ControlEmpresa.class.getName());

    private final static String INSERT_REPRESENTANTE = "INSERT INTO datos_representante(nombre_repre, apellido_pat_rep, apellido_mat_rep, "
            + "telefono_rep, celular_rep, email_rep, direccion_rep, foto_rep, codigo_postal, numero_ife, ctrl_curp) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String SELECT_REPRESENTANTES = "SELECT id_representante, nombre_repre, apellido_pat_rep,"
            + " apellido_mat_rep FROM datos_representante";

    private final static String INSERT_EMPRESA = "INSERT INTO datos_empresa(razon_social,anio_creacion, domicilio_razon, "
            + "codigo_postal, estado, telefono1, telefono2, fax, web, email_razon, nss, logo_empresa, id_representante, ciudad) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String SELECT_EMPRESA_ID_BY_REPRESENTANTE = "SELECT id_empresa FROM datos_empresa WHERE id_representante= ?";

    private final static String INSERT_CONTROL_EMPRESA = "INSERT INTO control_empresa(id_empresa, estatus_empresa, fecha_activa) "
            + "VALUES(?, ?, ?)";

    /**
     * Guarda un nuevo representante en la base de datos.
     * La operación se ejecuta en una transacción.
     *
     * @throws SQLException si ocurre un error de base de datos.
     * @throws IOException  si ocurre un error al procesar la imagen.
     */
    public static void guardaRepresentante(String nombreRepre, String apellidoPat, String apellidoMat,
                                           String telf, String cel, String email, String direccion,
                                           BufferedImage fotografia, String codigoPost, String numIfe, String curp)
            throws SQLException, IOException {

        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_REPRESENTANTE)) {
                // Convertir la imagen a un stream de bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(fotografia, "jpg", baos);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());

                pstmt.setString(1, nombreRepre);
                pstmt.setString(2, apellidoPat);
                pstmt.setString(3, apellidoMat);
                pstmt.setString(4, telf);
                pstmt.setString(5, cel);
                pstmt.setString(6, email);
                pstmt.setString(7, direccion);
                pstmt.setBinaryStream(8, is, is.available());
                pstmt.setString(9, codigoPost);
                pstmt.setString(10, numIfe);
                pstmt.setString(11, curp);

                pstmt.executeUpdate();
                conn.commit();
                LOGGER.info("Representante guardado exitosamente: " + nombreRepre);
            } catch (SQLException | IOException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error al guardar representante, se revirtió la transacción.", e);
                throw e; // Relanzar para que la UI pueda manejarlo
            }
        }
    }

    /**
     * Obtiene un mapa de todos los representantes.
     *
     * @return Un mapa donde la clave es el nombre completo del representante y el valor es su ID.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static Map<String, Integer> mostrarRepre() throws SQLException {
        Map<String, Integer> lista = new LinkedHashMap<>();
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_REPRESENTANTES);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id_rep = rs.getInt("id_representante");
                String nom = rs.getString("nombre_repre");
                String apellidoPat = rs.getString("apellido_pat_rep");
                String apellidoMat = rs.getString("apellido_mat_rep");
                String todo = String.join(" ", nom, apellidoPat, apellidoMat);
                lista.put(todo, id_rep);
            }
        }
        return lista;
    }

    /**
     * Guarda una nueva empresa en la base de datos.
     *
     * @throws SQLException si ocurre un error de base de datos.
     * @throws IOException  si ocurre un error al procesar la imagen del logo.
     */
    public static void guardarEmpresa(String razonSocial, String domicilio, String cp,
                                      String estado, String telef1, String telef2, String fax,
                                      String web, String email, String nss, BufferedImage logo,
                                      int representanteId, String ciudad) throws SQLException, IOException {
        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_EMPRESA)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(logo, "jpg", baos);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());

                pstmt.setString(1, razonSocial);
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setString(3, domicilio);
                pstmt.setString(4, cp);
                pstmt.setString(5, estado);
                pstmt.setString(6, telef1);
                pstmt.setString(7, telef2);
                pstmt.setString(8, fax);
                pstmt.setString(9, web);
                pstmt.setString(10, email);
                pstmt.setString(11, nss);
                pstmt.setBinaryStream(12, is, is.available());
                pstmt.setInt(13, representanteId);
                pstmt.setString(14, ciudad);

                pstmt.executeUpdate();
                conn.commit();
                LOGGER.info("Empresa guardada exitosamente: " + razonSocial);
            } catch (SQLException | IOException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error al guardar empresa, se revirtió la transacción.", e);
                throw e;
            }
        }
    }

    /**
     * Devuelve el ID de una empresa basado en el ID de su representante.
     *
     * @param representanteId El ID del representante.
     * @return Un {@link Optional<Integer>} con el ID de la empresa si se encuentra, o vacío en caso contrario.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static Optional<Integer> devuelveIDEmpresa(int representanteId) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EMPRESA_ID_BY_REPRESENTANTE)) {
            pstmt.setInt(1, representanteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(rs.getInt(1)) : Optional.empty();
            }
        }
    }

    /**
     * Inserta un registro de control para una empresa (activación/desactivación).
     *
     * @param empresaId El ID de la empresa.
     * @param status    El estado de la empresa (true para activa).
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static void controlaEmpresa(int empresaId, boolean status) throws SQLException {
        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_CONTROL_EMPRESA)) {
                pstmt.setInt(1, empresaId);
                pstmt.setBoolean(2, status);
                pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.executeUpdate();
                conn.commit();
                LOGGER.info("Registro de control para la empresa ID " + empresaId + " guardado.");
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error al guardar control de empresa, se revirtió la transacción.", e);
                throw e;
            }
        }
    }
}
