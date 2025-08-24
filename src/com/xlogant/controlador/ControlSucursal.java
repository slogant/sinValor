package com.xlogant.controlador;

import com.xlogant.conecta.ConectaDB;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de servicio (DAO) para gestionar las entidades Sucursal.
 * <p>
 * Esta clase es sin estado y utiliza métodos estáticos para interactuar con la base de datos.
 * Todas las operaciones de base de datos gestionan sus propios recursos de forma segura
 * y propagan las excepciones SQL para que sean manejadas por la capa de llamada.
 * 
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public final class ControlSucursal implements Serializable {

    @Serial
    private static final long serialVersionUID = -5022490814788863707L;
    private static final Logger LOGGER = Logger.getLogger(ControlSucursal.class.getName());
    private static final boolean ACTIVO = true;

    /**
     * Obtiene un mapa de empresas activas asociadas a un usuario.
     *
     * @param nombreUsuario El nombre del usuario.
     * @return Un mapa con el nombre de la empresa como clave y su ID como valor.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static Map<String, Long> sucursalesEmpresas(String nombreUsuario) throws SQLException {
        Map<String, Long> listaEmpresas = new TreeMap<>();
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CONSULTAEMPRESA)) {
            pstmt.setString(1, nombreUsuario);
            pstmt.setBoolean(2, ACTIVO);
            pstmt.setBoolean(3, ACTIVO);
            pstmt.setBoolean(4, ACTIVO);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long idEmpresa = rs.getLong("id_empresa");
                    String nombreEmpresa = rs.getString("razon_social");
                    listaEmpresas.put(nombreEmpresa, idEmpresa);
                }
            }
        }
        if (listaEmpresas.isEmpty()) {
            LOGGER.log(Level.WARNING, "No se encontraron empresas para el usuario: {0}", nombreUsuario);
        }
        return listaEmpresas;
    }

    /**
     * Obtiene un mapa de los encargados (usuarios) para una empresa y rol específicos.
     *
     * @param roleId    El ID del rol a buscar.
     * @param idEmpresa El ID de la empresa.
     * @return Un mapa con el ID del usuario como clave y su nombre completo como valor.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static Map<Long, String> devuelveEncargado(long roleId, long idEmpresa) throws SQLException {
        Map<Long, String> listaEncargados = new TreeMap<>();
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COLSULTAME)) {
            pstmt.setLong(1, roleId);
            pstmt.setLong(2, idEmpresa);
            pstmt.setBoolean(3, ACTIVO);
            pstmt.setBoolean(4, ACTIVO);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long idUsuario = rs.getLong("id_ctrol_usuario");
                    String nombre = rs.getString("nombre");
                    String apellidoPat = rs.getString("apellido_pat");
                    String apellidoMat = rs.getString("apellido_mat");
                    String nombreCompleto = String.join(" ", nombre, apellidoPat, apellidoMat);
                    listaEncargados.put(idUsuario, nombreCompleto);
                }
            }
        }
        return listaEncargados;
    }

    /**
     * Verifica si un número de sucursal ya existe para una empresa específica.
     *
     * @param idEmpresa El ID de la empresa.
     * @param numeroSuc El número de sucursal a verificar.
     * @return {@code true} si el número de sucursal ya existe, {@code false} en caso contrario.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static boolean isNumeroSucursalValidado(long idEmpresa, String numeroSuc) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(NUMSUCVERIFICADO)) {
            pstmt.setLong(1, idEmpresa);
            pstmt.setBoolean(2, ACTIVO);
            pstmt.setString(3, numeroSuc);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, el número ya existe.
            }
        }
    }

    /**
     * Guarda una nueva sucursal en la base de datos. La operación se ejecuta en una transacción.
     *
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static void guardaSucursal(long idEmpresa, String nomSuc, String direccionSuc,
                                      String cp, String tel1, String tel2, String email, String encargado, String numeroSuc, long idUsuario) throws SQLException {
        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(INSERTASUC)) {
                pstmt.setLong(1, idEmpresa);
                pstmt.setString(2, nomSuc);
                pstmt.setString(3, direccionSuc);
                pstmt.setString(4, cp);
                pstmt.setString(5, tel1);
                pstmt.setString(6, tel2);
                pstmt.setString(7, email);
                pstmt.setString(8, encargado);
                pstmt.setString(9, numeroSuc);
                pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setLong(11, idUsuario);
                pstmt.executeUpdate();
                conn.commit();
                LOGGER.info("Sucursal guardada exitosamente: " + nomSuc);
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error al guardar sucursal, se revirtió la transacción.", e);
                throw e;
            }
        }
    }

    /**
     * Devuelve el ID de una sucursal basado en su número único.
     *
     * @param numeroSucursal El número de la sucursal.
     * @return Un {@link Optional<Long>} con el ID de la sucursal si se encuentra, o vacío en caso contrario.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static Optional<Long> devuelveIDSucursal(String numeroSucursal) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DEVUELIDSUC)) {
            pstmt.setString(1, numeroSucursal);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(rs.getLong(1)) : Optional.empty();
            }
        }
    }

    /**
     * Inserta un registro de control para una sucursal (activación/desactivación).
     *
     * @param idSucursal El ID de la sucursal.
     * @param estado     El estado de la sucursal (true para activa).
     * @throws SQLException si ocurre un error de base de datos.
     */
    public static void insertaControl(long idSucursal, boolean estado) throws SQLException {
        try (Connection conn = ConectaDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(INSERTACONTROLSUC)) {
                pstmt.setLong(1, idSucursal);
                pstmt.setBoolean(2, estado);
                pstmt.executeUpdate();
                conn.commit();
                LOGGER.info("Registro de control para la sucursal ID " + idSucursal + " guardado.");
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error al guardar control de sucursal, se revirtió la transacción.", e);
                throw e;
            }
        }
    }

    private final static String CONSULTAEMPRESA = "SELECT ce.id_empresa, de.razon_social, "
            + "ce.estatus_empresa, ds.id_sucursal, "
            + "ds.nombre_sucursal,csc.estatus_suc "
            + "FROM datos_empresa  de "
            + "INNER JOIN control_empresa ce "
            + "ON ce.id_empresa = de.id_empresa "
            + "INNER JOIN datos_sucursal ds "
            + "ON ds.id_empresa = de.id_empresa "
            + "INNER JOIN control_suc csc "
            + "ON csc.id_suc = ds.id_sucursal "
            + "INNER JOIN datos_usuarios dus "
            + "ON dus.id_empresa = ce.id_empresa "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "WHERE du.usuario_nombre= ? "
            + "AND du.status_de_usuario= ? "
            + "AND ce.estatus_empresa= ? "
            + "AND csc.estatus_suc= ?";

    private final static String COLSULTAME = "SELECT dus.id_ctrol_usuario,dus.nombre, dus.apellido_pat, dus.apellido_mat, "
            + "dus.id_empresa, du.usuario_nombre, du.status_de_usuario "
            + "FROM datos_usuarios dus "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dus.id_empresa "
            + "INNER JOIN control_empresa ctrlemp "
            + "ON ctrlemp.id_empresa = demp.id_empresa "
            + "WHERE du.id_role= ? "
            + "AND dus.id_empresa= ? "
            + "AND du.status_de_usuario= ? "
            + "AND ctrlemp.estatus_empresa= ?";

    // Consulta corregida para ser más precisa.
    private final static String NUMSUCVERIFICADO = "SELECT dsuc.numero_suc FROM datos_sucursal dsuc "
            + "INNER JOIN control_empresa cemp ON cemp.id_empresa = dsuc.id_empresa "
            + "WHERE dsuc.id_empresa= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND dsuc.numero_suc= ?";

    private final static String INSERTASUC = "INSERT INTO datos_sucursal(id_empresa, nombre_sucursal, direccion_sucursal, "
            + "codigo_postal_suc, telefono1_suc, telefono2_suc, email_suc, encargado_suc, numero_suc, fecha_creacion, id_usuario) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String DEVUELIDSUC = "SELECT id_sucursal FROM datos_sucursal "
            + "WHERE numero_suc= ?";

    private final static String INSERTACONTROLSUC = "INSERT INTO control_suc(id_suc, estatus_suc) VALUES(?, ?)";

}
