package com.xlogant.controlador;

import com.xlogant.cambio.DatosPersonal;
import com.xlogant.conecta.ConectaDB;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de acceso a datos (DAO) para obtener información detallada de los usuarios.
 * <p>
 * Esta clase es un servicio sin estado; es seguro crear una instancia
 * y llamar a sus métodos. Utiliza try-with-resources para una gestión
 * segura de las conexiones a la base de datos.
 *
 * @author oscar (refactored by Gemini)
 */
public final class ControladorDatosUsuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 4543395262052813659L;
    private static final Logger LOGGER = Logger.getLogger(ControladorDatosUsuario.class.getName());

    private static final String BASE_QUERY = "SELECT DISTINCT dus.id_ctrol_usuario, dus.nombre, dus.apellido_pat, "
            + "dus.apellido_mat, dus.direccion_us, dus.telefono, dus.celular, "
            + "dus.email, dus.foto_usuario, dus.fecha_ingreso, dus.numero_emp, "
            + "da.nombre_sucursal, dus.rfid, ctr.nombre_role, du.status_de_usuario, "
            + "du.usuario_nombre, du.clave_usuario FROM datos_usuarios dus "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "INNER JOIN control_roles ctr "
            + "ON ctr.id_permiso = du.id_role "
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dus.id_empresa "
            + "INNER JOIN control_empresa cemp "
            + "ON cemp.id_empresa = demp.id_empresa "
            + "INNER JOIN datos_sucursal da "
            + "ON cemp.id_empresa = da.id_empresa "
            + "INNER JOIN control_suc csuc "
            + "ON dus.id_sucursal = da.id_sucursal "
            + "WHERE %s = ? " // Placeholder for the specific condition
            + "AND du.status_de_usuario= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND csuc.estatus_suc= ?";

    private static final String FIND_BY_EMP_NUMBER_QUERY = String.format(BASE_QUERY, "dus.numero_emp");
    private static final String FIND_BY_RFID_QUERY = String.format(BASE_QUERY, "dus.rfid");
    private static final String CREDENTIALS_MATCH_QUERY = "SELECT 1 FROM datos_usuario WHERE usuario_nombre = ? AND clave_usuario = ?";

    /**
     * Busca los datos de un usuario por su número de empleado.
     *
     * @param numeroEmpleado El número de empleado a buscar.
     * @return Una lista de {@link DatosPersonal} que coinciden.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public List<DatosPersonal> findByNumeroEmpleado(String numeroEmpleado) throws SQLException {
        return findUsers(FIND_BY_EMP_NUMBER_QUERY, numeroEmpleado);
    }

    /**
     * Busca los datos de un usuario por su número de RFID.
     *
     * @param numeroRFID El RFID a buscar.
     * @return Una lista de {@link DatosPersonal} que coinciden.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public List<DatosPersonal> findByRfid(String numeroRFID) throws SQLException {
        return findUsers(FIND_BY_RFID_QUERY, numeroRFID);
    }

    /**
     * Método genérico para ejecutar la consulta de búsqueda de usuarios.
     */
    private List<DatosPersonal> findUsers(String query, String parameter) throws SQLException {
        List<DatosPersonal> userList = new ArrayList<>();
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, parameter);
            pstmt.setBoolean(2, true);
            pstmt.setBoolean(3, true);
            pstmt.setBoolean(4, true);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userList.add(mapRowToDatosPersonal(rs));
                }
            }
        }
        LOGGER.info(String.format("Se encontraron %d usuarios para el parámetro '%s'", userList.size(), parameter));
        return userList;
    }

    /**
     * Verifica si las credenciales (nombre de usuario y clave) son correctas.
     *
     * @param username El nombre de usuario.
     * @param password La clave del usuario.
     * @return {@code true} si las credenciales coinciden, {@code false} en caso contrario.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public boolean credentialsMatch(String username, String password) throws SQLException {
        try (Connection conn = ConectaDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CREDENTIALS_MATCH_QUERY)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, las credenciales son correctas.
            }
        }
    }

    /**
     * Mapea la fila actual de un ResultSet a un objeto DatosPersonal.
     */
    private DatosPersonal mapRowToDatosPersonal(ResultSet rs) throws SQLException {
        int id_control = rs.getInt(1);
        String nom = rs.getString(2);
        String apelliPat = rs.getString(3);
        String apellidomat = rs.getString(4);
        String di = rs.getString(5);
        String te = rs.getString(6);
        String ce = rs.getString(7);
        String em = rs.getString(8);
        InputStream inp = rs.getBinaryStream(9);
        LocalDateTime tm = rs.getTimestamp(10).toLocalDateTime();
        String numem = rs.getString(11);
        String nomSuc = rs.getString(12);
        String rrfid = rs.getString(13);
        String role = rs.getString(14);
        boolean estado = rs.getBoolean(15);
        String us = rs.getString(16);
        String clv = rs.getString(17);

        return new DatosPersonal(id_control, nom, apelliPat,
                apellidomat, di, te, ce, em, inp,
                tm, numem, nomSuc, rrfid, role, estado, us, clv);
    }
}
