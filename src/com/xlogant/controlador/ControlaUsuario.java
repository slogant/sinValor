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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author oscar
 */
public final class ControlaUsuario implements Serializable {


    private final static String CONSULTA_DB = "SELECT * FROM datos_usuario "
            + "WHERE usuario_nombre= ? AND clave_usuario= ? "
            + "AND status_de_usuario= ?";

    private final static String LASEMPRESAS = "SELECT id_empresa FROM datos_usuario du "
            + "INNER JOIN datos_usuarios dus "
            + "ON dus.id_ctrol_usuario = du.id_dato_control "
            + "WHERE du.usuario_nombre = ? "
            + "AND du.clave_usuario = ?"
            + "AND du.status_de_usuario = ?";

    private final static String LACONSULTA = "SELECT du.usuario_nombre,du.clave_usuario, "
            + "du.status_de_usuario,cemp.estatus_empresa, "
            + "csuc.estatus_suc, da.nombre_sucursal "
            + "FROM datos_usuarios dus "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dus.id_empresa "
            + "INNER JOIN control_empresa cemp "
            + "ON cemp.id_empresa = demp.id_empresa "
            + "INNER JOIN datos_sucursal da "
            + "ON cemp.id_empresa = da.id_empresa "
            + "INNER JOIN control_suc csuc "
            + "ON csuc.id_suc = da.id_sucursal "
            + "WHERE du.usuario_nombre= ? "
            + "AND du.clave_usuario= ? "
            + "AND du.status_de_usuario= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND csuc.estatus_suc= ?";

    private final static String LACONSULTAPRINCIPAL = "SELECT du.usuario_nombre,du.clave_usuario, "
            + "du.status_de_usuario,cemp.estatus_empresa, "
            + "csuc.estatus_suc, da.nombre_sucursal, dus.id_sucursal "
            + "FROM datos_usuarios dus "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dus.id_empresa "
            + "INNER JOIN control_empresa cemp "
            + "ON cemp.id_empresa = demp.id_empresa "
            + "INNER JOIN datos_sucursal da "
            + "ON cemp.id_empresa = da.id_empresa "
            + "INNER JOIN control_suc csuc "
            + "ON csuc.id_suc = da.id_sucursal "
            + "WHERE du.usuario_nombre= ? "
            + "AND du.clave_usuario= ? "
            + "AND du.status_de_usuario= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND csuc.estatus_suc= ?";

    private final static String LACONSULTADELASSUC = "SELECT du.usuario_nombre, du.clave_usuario, "
            + "du.status_de_usuario, cemp.estatus_empresa, "
            + "csuc.estatus_suc, da.nombre_sucursal, dus.id_sucursal "
            + "FROM datos_usuarios dus "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "INNER JOIN datos_sucursal da "
            + "ON da.id_sucursal = dus.id_sucursal "
            + "INNER JOIN control_suc csuc "
            + "ON csuc.id_suc = da.id_sucursal "
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dus.id_empresa "
            + "INNER JOIN control_empresa cemp "
            + "ON cemp.id_empresa = demp.id_empresa "
            + "WHERE du.usuario_nombre= ? "
            + "AND du.clave_usuario= ? "
            + "AND du.status_de_usuario= ? "
            + "AND csuc.estatus_suc= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND dus.id_sucursal= ?";

    private final static String CONSULTA_ESTATUS = "SELECT * FROM datos_usuario "
            + "WHERE usuario_nombre= ? AND clave_usuario= ? ";
    private final static String INSERTDB = "INSERT INTO control_entradas(id_usuario, ip_maquina,"
            + " nombre_so, version_k,fecha_acceso, tipo, version_jdk, version_db,usuario_suc,vendor_java,num_procesadores, driver_name, version_driver)"
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String INSERTAINICIO = "INSERT INTO control_inicio(ip_maq, nombre_sop,"
            + " version_ker, total_deinicio, inicio_fecha, accion) "
            + " VALUES(?, ?, ?, ?, ?, ?)";

    @Serial
    private static final long serialVersionUID = -7644364981066767701L;
    private static final boolean ACTIVO = true;
    private static final String ENTRADA = "ENTRADA";

    /**
     * Verifica si las credenciales de un usuario son válidas y si el usuario,
     * su empresa y su sucursal están activos.
     *
     * @param nombre el nombre de usuario.
     * @param claves la clave ya encriptada.
     * @return {@code true} si el login es válido, {@code false} en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static boolean login(String nombre, String claves) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LACONSULTA)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, ACTIVO);
            pstmt.setBoolean(4, ACTIVO);
            pstmt.setBoolean(5, ACTIVO);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, el login es válido.
            }
        }
    }

    /**
     * Verifica si un usuario está marcado como activo en la tabla de usuarios.
     *
     * @param nombre el nombre de usuario.
     * @param claves la clave ya encriptada.
     * @return {@code true} si el usuario está activo, {@code false} en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static boolean usuarioActivo(String nombre, String claves) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CONSULTA_ESTATUS)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("status_de_usuario");
                }
                return false; // No se encontró el usuario.
            }
        }
    }

    /**
     * Obtiene el ID del rol de un usuario.
     *
     * @return el ID del rol, o 0 si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static long obtenerRole(String nombre, String claves, boolean activo) throws SQLException {
        return obtenerLongDesdeTabla(nombre, claves, activo, "id_role");
    }

    /**
     * Obtiene el ID de control de un usuario.
     *
     * @return el ID del usuario, o 0 si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static long obtenerID(String nombre, String claves, boolean activo) throws SQLException {
        return obtenerLongDesdeTabla(nombre, claves, activo, "id_dato_control");
    }

    private static long obtenerLongDesdeTabla(String nombre, String claves, boolean activo, String columnName) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CONSULTA_DB)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, activo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getLong(columnName) : 0L;
            }
        }
    }

    /**
     * Obtiene el ID de la empresa asociada a un usuario.
     *
     * @return el ID de la empresa, o 0 si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static long obtenerIDEmp(String nombre, String claves, boolean activo) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LASEMPRESAS)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, activo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getLong("id_empresa") : 0L;
            }
        }
    }

    /**
     * Obtiene el nombre de un usuario.
     *
     * @return el nombre del usuario, o una cadena vacía si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static String obtenerPorNombre(String nombre, String claves, boolean activo) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CONSULTA_DB)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, activo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getString("usuario_nombre") : "";
            }
        }
    }

    /**
     * Obtiene el ID de la sucursal de un usuario.
     *
     * @return el ID de la sucursal, o 0 si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static long IDSUCURSAL(String nombre, String claves) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LACONSULTAPRINCIPAL)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, ACTIVO);
            pstmt.setBoolean(4, ACTIVO);
            pstmt.setBoolean(5, ACTIVO);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getLong("id_sucursal") : 0L;
            }
        }
    }

    /**
     * Obtiene el nombre de la sucursal de un usuario.
     *
     * @return el nombre de la sucursal, o una cadena vacía si no se encuentra.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static String NombreSuc(String nombre, String claves, long sucursal) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(LACONSULTADELASSUC)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, claves);
            pstmt.setBoolean(3, ACTIVO);
            pstmt.setBoolean(4, ACTIVO);
            pstmt.setBoolean(5, ACTIVO);
            pstmt.setLong(6, sucursal);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getString("nombre_sucursal") : "";
            }
        }
    }

    // Los métodos de inserción (auditoría) deberían idealmente estar en su propio servicio.
    // Se mantienen aquí por consistencia con la clase original, pero refactorizados.

    public static void insertaIngreso(long idUsuario, String ip, String so, String kernel, String versionJdk, String sucursal, String vendorJava, String numProcesadores) throws SQLException {
        String insertSQL = "INSERT INTO control_entradas(id_usuario, ip_maquina, nombre_so, version_k, fecha_acceso, tipo, version_jdk, version_db, usuario_suc, vendor_java, num_procesadores, driver_name, version_driver) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConectaDB.getConnection()) {
            // La transacción se maneja aquí para asegurar la atomicidad.
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                var meta = conn.getMetaData();
                pstmt.setLong(1, idUsuario);
                pstmt.setString(2, ip);
                pstmt.setString(3, so);
                pstmt.setString(4, kernel);
                pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                pstmt.setString(6, ENTRADA);
                pstmt.setString(7, versionJdk);
                pstmt.setString(8, meta.getDatabaseProductVersion());
                pstmt.setString(9, sucursal);
                pstmt.setString(10, vendorJava);
                pstmt.setString(11, numProcesadores);
                pstmt.setString(12, meta.getDriverName());
                pstmt.setString(13, meta.getDriverVersion());
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e; // Relanzar la excepción para que el llamador sepa que falló.
            }
        }
    }
}
