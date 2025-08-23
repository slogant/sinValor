/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.xlogant.conecta.ConectaDB;

import java.io.*;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xlogant.conecta.ConectaDB.cerrarCommit;
import static com.xlogant.conecta.ConectaDB.obtenerConexion;
import static java.lang.System.out;
import static java.sql.Timestamp.*;
import static java.time.LocalDateTime.*;

/**
 * @author oscar
 */
public final class ControlaUsuario implements Serializable {

    public static boolean login(String nombre, String claves) {
        var caracteres = claves.toCharArray();
        var conectadoss = obtenerConexion();
        var devuelveVerdad = false;
        try (conectadoss;
             var pstmn = conectadoss.prepareStatement(LACONSULTA)) {
            pstmn.setString(1, nombre);
            pstmn.setString(2, claves);
            pstmn.setBoolean(3, ACTIVO);
            pstmn.setBoolean(4, ACTIVO);
            pstmn.setBoolean(5, ACTIVO);
            var resultados = pstmn.executeQuery();
            try (resultados) {
                if (resultados.next()) {
                    Nombres = resultados.getString(1);
                    Clave = resultados.getString(2);
                    activos = resultados.getBoolean(3);
                    actividad = resultados.getBoolean(4);
                    actividades = resultados.getBoolean(5);
                    valorando = Clave.toCharArray();
                }
                return devuelveVerdad = (nombre.equals(Nombres)
                        && (Arrays.equals(caracteres, valorando))
                        && (activos == ACTIVO)
                        && (actividad == ACTIVO)
                        && (actividades == ACTIVO));
            }
        } catch (SQLException e) {
            out.println("Error en login: " + e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean usuarioActivo(String nombre, String claves) {
        var conectadoss = obtenerConexion();
        try (conectadoss;
             var pstmn = conectadoss.prepareStatement(CONSULTA_ESTATUS);) {
            pstmn.setString(1, nombre);
            pstmn.setString(2, claves);
            var resultados = pstmn.executeQuery();
            try (resultados) {
                if (resultados.next()) {
                    activos = resultados.getBoolean(4);
                }
                return activos;
            }
        } catch (SQLException e) {
            out.println("Error usuario activo: " + e.getLocalizedMessage());
            return false;
        }
    }

    public static long obtenerRole(String nombre, String claves, boolean activo) {

        var caracteres = claves.toCharArray();
        var conexion = obtenerConexion();
        try (conexion;
             var pstm = conexion.prepareStatement(CONSULTA_DB)) {
            pstm.setString(1, nombre);
            pstm.setString(2, claves);
            pstm.setBoolean(3, activo);
            var resultado = pstm.executeQuery();
            try (resultado) {
                if (resultado.next()) {
                    id = resultado.getLong(1);
                    Nombres = resultado.getString(2);
                    Clave = resultado.getString(3);
                    activos = resultado.getBoolean(4);
                    rol = resultado.getInt(5);
                    valorando = Clave.toCharArray();
                }
                if (nombre.equals(Nombres) && Arrays.equals(caracteres, valorando) && activos == activo) {
                    return rol;
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            out.println("Error en role: " + e.getLocalizedMessage());
            return 0;
        }
    }

    public static long obtenerID(String nombre, String claves, boolean activo) {

        var caracteres = claves.toCharArray();
        var conexion = obtenerConexion();
        try (conexion;
             var pstm = conexion.prepareStatement(CONSULTA_DB);) {
            pstm.setString(1, nombre);
            pstm.setString(2, claves);
            pstm.setBoolean(3, activo);
            var resultado = pstm.executeQuery();
            try (resultado) {
                if (resultado.next()) {
                    id = resultado.getLong(1);
                    Nombres = resultado.getString(2);
                    Clave = resultado.getString(3);
                    activos = resultado.getBoolean(4);
                    rol = resultado.getLong(5);
                    valorando = Clave.toCharArray();
                }
                if (nombre.equals(Nombres) && Arrays.equals(caracteres, valorando) && activos == activo) {
                    return id;
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            out.println("Error en id:  " + e.getLocalizedMessage());
            return 0;
        }
    }

    public static long obtenerIDEmp(String nombre, String claves, boolean activo) {

        var conn = obtenerConexion();
        try (conn; var pm = conn.prepareStatement(LASEMPRESAS);) {
            pm.setString(1, nombre);
            pm.setString(2, claves);
            pm.setBoolean(3, activo);
            var ret = pm.executeQuery();
            try (ret) {
                while (ret.next()) {
                    id = ret.getLong(1);
                }
                if (id > 0) {
                    return id;
                } else {
                    return 0L;
                }
            }
        } catch (SQLException e) {

            return 0L;
        }
    }

    public static String obtenerPorNombre(String nombre, String claves, boolean activo) {

        var caracteres = claves.toCharArray();
        var ctn = obtenerConexion();
        try (ctn; var psts = ctn.prepareStatement(CONSULTA_DB);) {
            psts.setString(1, nombre);
            psts.setString(2, claves);
            psts.setBoolean(3, activo);
            var resulta = psts.executeQuery();
            try (resulta) {
                if (resulta.next()) {
                    id = resulta.getLong(1);
                    Nombres = resulta.getString(2);
                    Clave = resulta.getString(3);
                    activos = resulta.getBoolean(4);
                    rol = resulta.getLong(5);
                    valorando = Clave.toCharArray();
                }
                if (nombre.equals(Nombres) && Arrays.equals(caracteres, valorando) && activos == activo) {
                    return Nombres;
                } else {
                    return "";
                }
            }
        } catch (SQLException e) {
            out.println("Error nombre: " + e.getLocalizedMessage());
            Arrays.fill(valorando, '0');
            return "";
        }
    }

    public static boolean insertaIngreso(long idUsuario, String ip, String SO,
                                         String K, String entrada, String version_jdk, String suc,
                                         String vendorJava, String numeroProcesadores) {

        var ldt = now();
        var t = valueOf(ldt);
        var concretado = 0;
        var versionDB = versionPostgresql();
        var con = obtenerConexion();
        out.println(".............................." + versionDB);
        try (con;
             var pst = con.prepareStatement(INSERTDB);) {
            var meta = con.getMetaData();
            pst.setLong(1, idUsuario);
            pst.setString(2, ip);
            pst.setString(3, SO);
            pst.setString(4, K);
            pst.setTimestamp(5, t);
            pst.setString(6, entrada);
            pst.setString(7, version_jdk);
            pst.setString(8, versionDB);
            pst.setString(9, suc);
            pst.setString(10, vendorJava);
            pst.setString(11, numeroProcesadores);
            pst.setString(12, meta.getDriverName());
            pst.setString(13, meta.getDriverVersion());
            concretado = pst.executeUpdate();
            cerrarCommit(con);
            out.println("Se a ejecutado.............. " + concretado);
            return (concretado == 1);
        } catch (SQLException e) {
            out.println("Error en insert ingreso " + e.getLocalizedMessage());
            out.println(e.getCause());
            return false;
        }
    }

    public static boolean insertaInicio(String ipMaquina, String nomSO,
                                        String versionK, String totalInicio) {
        var ldtm = now();
        var tp = valueOf(ldtm);
        var conec = obtenerConexion();
        try (conec;
             var pp = conec.prepareStatement(INSERTAINICIO);) {
            pp.setString(1, ipMaquina);
            pp.setString(2, nomSO);
            pp.setString(3, versionK);
            pp.setString(4, totalInicio);
            pp.setTimestamp(5, tp);
            pp.setString(6, ENTRADA);
            int hecho = pp.executeUpdate();
            cerrarCommit(conec);
            return (hecho == 1);
        } catch (SQLException ex) {
            Logger.getLogger(ControlaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            out.println("error en insert acceso: " + ex.getLocalizedMessage());
            out.println(ex.getCause());
            ConectaDB.rollback(conec);
            return false;
        }
    }

    public static String versionPostgresql() {
        //ProcessBuilder processBuilder = new ProcessBuilder();

        // Run this on Windows, cmd, /c = terminate after this run
       /* processBuilder.command("ping -c 3 google.com");

        try {

            Process process = processBuilder.start();

            // blocked :(
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "Hola";
        //try {
         /*   String almacena, devuelveVersion = null;
            var processBuilder = new ProcessBuilder();
            var post = processBuilder.command("psql --version");
        Process process = null;
        try {
            process = post.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        /*try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((devuelveVersion = reader.readLine()) != null) {
                    System.out.println(devuelveVersion);
                    almacena = devuelveVersion;
                }
            return almacena;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            /*try(var stdio = new BufferedReader(new InputStreamReader(post.start().getInputStream()));
            var stdeo = new BufferedReader(new InputStreamReader(post.start().getErrorStream()));) {
                while ((almacena = stdio.readLine()) != null) {
                    devuelveVersion = almacena;
                }
                while ((almacena = stdeo.readLine()) != null) {
                    devuelveVersion = almacena;
                }
                return devuelveVersion;
            }*/
        /*} catch (IOException ex) {
            Logger.getLogger(ControlaUsuario.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex.getLocalizedMessage());
            out.println(ex.getCause());
            return "";
        }*/
    }

    public static long IDSUCURSAL(String nombre, String claves) {
        var caracteres = claves.toCharArray();
        var conectadoss = obtenerConexion();
        try (conectadoss;
             var pstmn = conectadoss.prepareStatement(LACONSULTAPRINCIPAL);) {
            pstmn.setString(1, nombre);
            pstmn.setString(2, claves);
            pstmn.setBoolean(3, ACTIVO);
            pstmn.setBoolean(4, ACTIVO);
            pstmn.setBoolean(5, ACTIVO);
            var resultados = pstmn.executeQuery();
            try (resultados) {
                if (resultados.next()) {
                    Nombres = resultados.getString(1);
                    Clave = resultados.getString(2);
                    activos = resultados.getBoolean(3);
                    actividad = resultados.getBoolean(4);
                    actividades = resultados.getBoolean(5);
                    sucursales = resultados.getString(6);
                    unId = resultados.getLong(7);
                    valorando = Clave.toCharArray();
                }
                if (nombre.equals(Nombres)
                        && (Arrays.equals(caracteres, valorando))
                        && (activos == ACTIVO)
                        && (actividad == ACTIVO)
                        && (actividades == ACTIVO)
                        && !sucursales.isEmpty()) {
                    return unId;
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            out.println("Error id sucursal: " + e.getLocalizedMessage());
            return 0;
        }
    }

    public static String NombreSuc(String nombre, String claves, long sucursal) {
        var caracteres = claves.toCharArray();
        var cnts = obtenerConexion();
        try (cnts;
             var prepare = cnts.prepareStatement(LACONSULTADELASSUC);) {
            prepare.setString(1, nombre);
            prepare.setString(2, claves);
            prepare.setBoolean(3, ACTIVO);
            prepare.setBoolean(4, ACTIVO);
            prepare.setBoolean(5, ACTIVO);
            prepare.setLong(6, sucursal);
            var rest = prepare.executeQuery();
            try (rest) {
                while (rest.next()) {
                    Nombres = rest.getString(1);
                    Clave = rest.getString(2);
                    activos = rest.getBoolean(3);
                    actividad = rest.getBoolean(4);
                    actividades = rest.getBoolean(5);
                    sucursales = rest.getString(6);
                    valorando = Clave.toCharArray();
                }
                if (nombre.equals(Nombres)
                        && (Arrays.equals(caracteres, valorando))
                        && (activos == ACTIVO)
                        && (actividad == ACTIVO)
                        && (actividades == ACTIVO)) {
                    return sucursales;
                } else {
                    out.println("Esto esta vacio");
                    return "";
                }
            }
        } catch (SQLException e) {
            out.println("Error en nombre sucursal: " + e.getLocalizedMessage());
            out.println("El error es: " + e.getSQLState());
            return "";
        }
    }

    @Serial
    private static final long serialVersionUID = -7644364981066767701L;
    private static char[] valorando;
    private static long id;
    private static long unId;
    private static String Nombres, Clave;
    private static String sucursales;
    private static boolean activos, actividad, actividades;
    private static final boolean ACTIVO = true;
    private static long rol;
    private final static String ENTRADA = "ENTRADA";

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
}
