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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import static com.xlogant.conecta.ConectaDB.*;
import static java.sql.Timestamp.*;
import static java.time.LocalDateTime.*;

/**
 *
 * @author oscar
 */
public final class ControlSucursal implements Serializable {

    public static Map<String, Long> sucursalesEmpresas(String nombre) {
        try {
            Map<String, Long> listame = new TreeMap<>();
            conecta = obtenerConexion();
            pstm = conecta.prepareStatement(CONSULTAEMPRESA);
            pstm.setString(1, nombre);
            pstm.setBoolean(2, ACT);
            pstm.setBoolean(3, ACT);
            pstm.setBoolean(4, ACT);
            resultado = pstm.executeQuery();
            while (resultado.next()) {
                long idemp = resultado.getLong(1);
                String nombreEmpresas = resultado.getString(2);
                listame.put(nombreEmpresas, idemp);
            }
            if (listame.isEmpty()) {
                System.out.println("No se cargaron los registros de la empresa");
                listame.put("Registro no encontrado", 0L);
                return listame;
            } else {
                return listame;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            return null;
        } finally {
            cerrarPreparaStatement(pstm);
            cerrarResultSet(resultado);
            cerrarConexion(conecta);
        }
    }

    public static Map<Long, String> devuelveEncargado(long roles, long idEmpresa) {
        try {
            listalosEncargados = new TreeMap<>();
            Connection conectados = obtenerConexion();
            PreparedStatement ps = conectados.prepareStatement(COLSULTAME);
            ps.setLong(1, roles);
            ps.setLong(2, idEmpresa);
            ps.setBoolean(3, ACT);
            ps.setBoolean(4, ACT);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                var nombre = res.getString(2);
                var apellidopat = res.getString(3);
                var apellidomat = res.getString(4);
                var idrols = res.getLong(8);
                var unido = nombre + " " + apellidopat + " " + apellidomat;
                listalosEncargados.put(idrols, unido);
            }
            if (listalosEncargados.isEmpty()) {
                listalosEncargados.put(0L, "Valor no encontrado");
                return listalosEncargados;
            } else {
                return listalosEncargados;
            }
        } catch (SQLException ex) {
            System.out.println("Error: -> " + ex.getLocalizedMessage());
            listalosEncargados.put(0L, ex.getSQLState());
            return listalosEncargados;
        }
    }
    
    public static boolean NumSucValidado(long numero, String numeroSuc) {
        try {
            cnt = obtenerConexion();
            ppstm = cnt.prepareStatement(NUMSUCVERIFICADO);
            ppstm.setLong(1, numero);
            ppstm.setBoolean(2, ACT);
            ppstm.setString(3, numeroSuc);
            rest = ppstm.executeQuery();
            while (rest.next()) {                
                elnumerosuc = rest.getString(1);
            }
            return numeroSuc.equals(elnumerosuc);
        } catch(SQLException e) {
            System.out.println("Error: " + e.getLocalizedMessage());
            return false;
        } finally {
            cerrarPreparaStatement(ppstm);
            cerrarResultSet(rest);
            cerrarConexion(cnt);
        }
    }

    public static boolean guardaSucursal(long idem, String nomSuc, String direccionSuc,
            String cp, String tel1, String tel2, String emails, String enc, String numeroSuc, long idusua) {
        var fechaActual = now();
        var timestamp = valueOf(fechaActual);
        try {
            con = obtenerConexion();
            pst = con.prepareStatement(INSERTASUC);
            pst.setLong(1, idem);
            pst.setString(2, nomSuc);
            pst.setString(3, direccionSuc);
            pst.setString(4, cp);
            pst.setString(5, tel1);
            pst.setString(6, tel2);
            pst.setString(7, emails);
            pst.setString(8, enc);
            pst.setString(9, numeroSuc);
            pst.setTimestamp(10, timestamp);
            pst.setLong(11, idusua);
            int realizado = pst.executeUpdate();
            cerrarCommit(con);
            return (realizado == 1);
        } catch(SQLException ex) {
            System.out.println("Error al guardar la sucursal: " + ex.getLocalizedMessage());
            System.out.println("Error: " + ex.getSQLState());
            rollback(con);
            return false;
        } finally {
            cerrarPreparaStatement(pst);
            cerrarConexion(con);
        }
    }
    
    public static long devuelveIDSucursal(String numeroSucursales){
        try {
            conec = obtenerConexion();
            ptm = conec.prepareStatement(DEVUELIDSUC);
            ptm.setString(1, numeroSucursales);
            resulta = ptm.executeQuery();
            if (resulta.next()) {                
                elidentificadorSuc = resulta.getLong(1);
            }
            if(elidentificadorSuc > 0L ) {
                return  elidentificadorSuc;
            } else {
                System.out.println("EL numero de sucursal no se encontro");
                return 0L;
            }
        } catch(SQLException ex) {
            System.out.println("Error al buscar el identificador de sucursal\n" + ex.getLocalizedMessage());
            System.out.println("Error: ----> " + ex.getSQLState());
            return 0L;
        } finally {
            cerrarPreparaStatement(ptm);
            cerrarResultSet(resulta);
            cerrarConexion(conec);
        }
    }
    
    public static boolean insertaControl(long suc, boolean edos) {
        try {
            cnn = obtenerConexion();
            psts = cnn.prepareStatement(INSERTACONTROLSUC);
            psts.setLong(1, suc);
            psts.setBoolean(2, edos);
            var hecho = psts.executeUpdate();
            cerrarCommit(cnn);
            return (hecho == 1);
        } catch(SQLException ex) {
            System.out.println("Error al guardar el control de la sucursal\n" +ex.getLocalizedMessage());
            System.out.println("Error______---> " + ex.getSQLState());
            rollback(cnn);
            return false;
        } finally {
            cerrarPreparaStatement(psts);
            cerrarConexion(cnn);
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
            + "dus.id_empresa, du.usuario_nombre, du.status_de_usuario, du.id_role "
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
    
    
    private final static String NUMSUCVERIFICADO = "SELECT dsuc.numero_suc FROM datos_sucursal dsuc "
            + "INNER JOIN control_suc csuc " 
            + "ON csuc.id_suc = dsuc.id_sucursal " 
            + "INNER JOIN datos_empresa demp "
            + "ON demp.id_empresa = dsuc.id_empresa " 
            + "INNER JOIN control_empresa cemp " 
            + "ON cemp.id_empresa = demp.id_empresa " 
            + "WHERE demp.id_empresa= ? " 
            + "AND cemp.estatus_empresa= ?"
            + "AND dsuc.numero_suc= ?";
    
    private final static String INSERTASUC = "INSERT INTO datos_sucursal(id_empresa, nombre_sucursal, direccion_sucursal, "
            + "codigo_postal_suc, telefono1_suc, telefono2_suc, email_suc, encargado_suc, numero_suc, fecha_creacion, id_usuario) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private final static String DEVUELIDSUC = "SELECT id_sucursal FROM datos_sucursal "
            + "WHERE numero_suc= ?";
    
    private final static String INSERTACONTROLSUC = "INSERT INTO control_suc(id_suc, estatus_suc) VALUES(?, ?)";

    private static long elidentificadorSuc;
    private static String elnumerosuc;
    private static final boolean ACT = true;
    private static Map<Long, String> listalosEncargados;
    private static Connection conecta;
    private static Connection cnt;
    private static Connection con;
    private static Connection conec;
    private static Connection cnn;
    private static PreparedStatement pstm;
    private static PreparedStatement ppstm;
    private static PreparedStatement pst;
    private static PreparedStatement ptm;
    private static PreparedStatement psts;
    private static ResultSet resultado;
    private static ResultSet rest;
    private static ResultSet resul;
    private static ResultSet resulta;
    @Serial
    private static final long serialVersionUID = -5022490814788863707L;

}
