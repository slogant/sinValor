package com.xlogant.controlador;

import com.xlogant.cambio.DatosPersonal;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xlogant.conecta.ConectaDB.cerrarConexion;
import static com.xlogant.conecta.ConectaDB.obtenerConexion;

/**
 *
 * @author antonio
 */
public final class ControladorDatosUsuario implements Serializable {

    public static ArrayList<DatosPersonal> muestraDatosUsuario(String numeroEmpleado) {
        ArrayList<DatosPersonal> lista = new ArrayList<>();
        try {
            conectados = obtenerConexion();
            prepareme = conectados.prepareStatement(CONSULTANDO);
            prepareme.setString(1, numeroEmpleado);
            prepareme.setBoolean(2, true);
            prepareme.setBoolean(3, true);
            prepareme.setBoolean(4, true);
            resultadosx = prepareme.executeQuery();
            while (resultadosx.next()) {
                int id_control = resultadosx.getInt(1);
                String nom = resultadosx.getString(2);
                String apelliPat = resultadosx.getString(3);
                String apellidomat = resultadosx.getString(4);
                String di = resultadosx.getString(5);
                String te = resultadosx.getString(6);
                String ce = resultadosx.getString(7);
                String em = resultadosx.getString(8);
                InputStream inp = resultadosx.getBinaryStream(9);
                LocalDateTime tm = resultadosx.getTimestamp(10).toLocalDateTime();
                String numem = resultadosx.getString(11);
                String nomSuc = resultadosx.getString(12);
                String rrfid = resultadosx.getString(13);
                String role = resultadosx.getString(14);
                boolean estado = resultadosx.getBoolean(15);
                String us = resultadosx.getString(16);
                String clv = resultadosx.getString(17);
                lista.add(new DatosPersonal(id_control, nom, apelliPat,
                        apellidomat, di, te, ce, em, inp,
                        tm, numem, nomSuc, rrfid, role, estado, us, clv));
            }
            System.out.println("Tamaño: " + lista.size());
            return lista;
        } catch (SQLException e) {
            System.out.println("Error: ----------> " + e.getLocalizedMessage());
            return null;
        } finally {
            if (conectados != null) {
                cerrarConexion(conectados);
            }
            if (prepareme != null) {
                try {
                    prepareme.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorDatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error: ----------> " + ex.getLocalizedMessage());
                }
            }
            if (resultadosx != null) {
                try {
                    resultadosx.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorDatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error: ----------> " + ex.getLocalizedMessage());
                }
            }
        }
    }

    public static ArrayList<DatosPersonal> cargandoDatosPersona(String numeroRFID) {
        ArrayList<DatosPersonal> lalista = new ArrayList<>();
        try {
            conec = obtenerConexion();
            ps = conec.prepareStatement(CONSULTANDOPORRF, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, numeroRFID);
            ps.setBoolean(2, true);
            ps.setBoolean(3, true);
            ps.setBoolean(4, true);
            res = ps.executeQuery();
            while (res.next()) {
                int id_controls = res.getInt(1);
                String noms = res.getString(2);
                String apelliPats = res.getString(3);
                String apellidomats = res.getString(4);
                String dis = res.getString(5);
                String tes = res.getString(6);
                String ces = res.getString(7);
                String ems = res.getString(8);
                InputStream inps = res.getBinaryStream(9);
                LocalDateTime tms = res.getTimestamp(10).toLocalDateTime();
                String numems = res.getString(11);
                String nomSucs = res.getString(12);
                String rrfids = res.getString(13);
                String rol = res.getString(14);
                boolean estados = res.getBoolean(15);
                String uss = res.getString(16);
                String clvs = res.getString(17);
                lalista.add(new DatosPersonal(id_controls, noms, apelliPats,
                        apellidomats, dis, tes, ces, ems, inps,
                        tms, numems, nomSucs, rrfids, rol, estados, uss, clvs));
            }
            System.out.printf("Tamaño: %d%n ", lalista.size());
            return lalista;
        } catch (SQLException e) {
            System.out.printf("Error: ----------> %s%n", e.getLocalizedMessage());
            return null;
        } finally {
            if (conec != null) {
                cerrarConexion(conec);
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorDatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.printf("Error: ----------> %s%n" , ex.getLocalizedMessage());
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorDatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.printf("Error: ----------> %s%n" + ex.getLocalizedMessage());
                }
            }
        }
    }

    public static boolean claveEnBase(String nombreS, String clave) {
        var claves = clave.toCharArray();
        try {
            Connection conecta = obtenerConexion();
            PreparedStatement pstm = conecta.prepareStatement(TRAEPASS);
            pstm.setString(1, nombreS);
            pstm.setString(2, clave);
            res = pstm.executeQuery();
            while (res.next()) {
                nombreUs = res.getString(1);
                String claveUs = res.getString(2);
                valorar = claveUs.toCharArray();
            }
            return (nombreS.equals(nombreUs) && Arrays.equals(claves, valorar));
        } catch (SQLException ex) {
            Logger.getLogger(ControladorDatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.printf("Error: ----------> %n%s" + ex.getLocalizedMessage());
            return false;
        }
    }

    @Serial
    private static final long serialVersionUID = 4543395262052813659L;

    private static final String CONSULTANDO = "SELECT DISTINCT dus.id_ctrol_usuario, dus.nombre, dus.apellido_pat, "
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
            + "WHERE dus.numero_emp= ? "
            + "AND du.status_de_usuario= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND csuc.estatus_suc= ?";

    private static final String CONSULTANDOPORRF = "SELECT DISTINCT dus.id_ctrol_usuario, dus.nombre, dus.apellido_pat, "
            + "dus.apellido_mat, dus.direccion_us, dus.telefono, dus.celular, "
            + "dus.email, dus.foto_usuario, dus.fecha_ingreso, dus.numero_emp, "
            + "da.nombre_sucursal, dus.rfid, ctr.nombre_role, du.status_de_usuario, "
            + " du.usuario_nombre, du.clave_usuario FROM datos_usuarios dus "
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
            + "WHERE dus.rfid= ? "
            + "AND du.status_de_usuario= ? "
            + "AND cemp.estatus_empresa= ? "
            + "AND csuc.estatus_suc= ?";

    private static final String TRAEPASS = "SELECT du.usuario_nombre, du.clave_usuario FROM datos_usuario du WHERE du.usuario_nombre= ? AND du.clave_usuario= ?";

    private static String numeroemp;
    private static Connection conectados;
    private static Connection conec;
    private static PreparedStatement prepareme;
    private static PreparedStatement ps;
    private static ResultSet resultadosx, res, resulta;
    private static int idemp;
    private static int idsuc;
    private static String nombreUs;

    private static int idemps;
    private static int idsucs;
    private static String nombreUss;
    private static String claveUss;
    ;
    private static char[] valorar;
}
