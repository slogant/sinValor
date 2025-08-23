/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static com.xlogant.conecta.ConectaDB.*;

/**
 *
 * @author oscar
 */
public final class ControlEmpresa implements Serializable {
    
    public static boolean guardaRepresentante(String nombreRepre, String apellidoPat, String apellidoMat,
            String telf, String cel, String email, String direccion,
            BufferedImage fotografia, String codigoPost, String numIfe, String curp) {
        try {
            conex = obtenerConexion();
            imagenDelUsuario = fotografia;
            byatos = new ByteArrayOutputStream();
            ImageIO.write(imagenDelUsuario, "jpg", byatos);
            inputStream = new ByteArrayInputStream(byatos.toByteArray());
            pstmn = conex.prepareStatement(INSERTA);
            pstmn.setString(1, nombreRepre);
            pstmn.setString(2, apellidoPat);
            pstmn.setString(3, apellidoMat);
            pstmn.setString(4, telf);
            pstmn.setString(5, cel);
            pstmn.setString(6, email);
            pstmn.setString(7, direccion);
            pstmn.setBinaryStream(8, inputStream, inputStream.available());
            pstmn.setString(9, codigoPost);
            pstmn.setString(10, numIfe);
            pstmn.setString(11, curp);
            concretado = pstmn.executeUpdate();
            cerrarCommit(conex);
            System.out.println("Se a ejecutado.............. " + concretado);
            return concretado == 1;
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ControlEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            rollback(conex);
            return false;
        } finally {
            cerrarPreparaStatement(pstmn);
            cerrarConexion(conex);
        }
    }
    
    public static Map<String, Integer>  mostrarRepre() {
        try {
            Map<String, Integer> lista = new LinkedHashMap<>();
            con = obtenerConexion();
            pss = con.prepareStatement(MUESTRAREPRESENTANTE);
            rest = pss.executeQuery();
            while (rest.next()) {
                int id_rep = rest.getInt(1);
                String nom = rest.getString(2);
                String apellidoPat = rest.getString(3);
                String apellidoMat = rest.getString(4);
                String todo = nom + " " + apellidoPat + " " + apellidoMat;
                lista.put(todo, id_rep);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ControlEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            return null;
        } finally {
            cerrarPreparaStatement(pss);
            cerrarResultSet(rest);
            cerrarConexion(con);
        }
    }
    
    public static boolean guardarEmpresa(String razonSocial, String Domicilio, String CP,
            String Estado, String telef1, String telef2, String Fax,
            String Web, String Email, String Nss, BufferedImage logos,
            int ids, String ciudad) {
       try {
            var ldt = LocalDateTime.now();
            var t = Timestamp.valueOf(ldt);
            imagenDelUsuario = logos;
            byatos = new ByteArrayOutputStream();
            ImageIO.write(imagenDelUsuario, "jpg", byatos);
            inputStream = new ByteArrayInputStream(byatos.toByteArray());
            con = obtenerConexion();
            pstmn = con.prepareStatement(INSERTAEMPRESA);
            pstmn.setString(1, razonSocial);
            pstmn.setTimestamp(2, t);
            pstmn.setString(3, Domicilio);
            pstmn.setString(4, CP);
            pstmn.setString(5, Estado);
            pstmn.setString(6, telef1);
            pstmn.setString(7, telef2);
            pstmn.setString(8, Fax);
            pstmn.setString(9, Web);
            pstmn.setString(10, Email);
            pstmn.setString(11, Nss);
            pstmn.setBinaryStream(12, inputStream, inputStream.available());
            pstmn.setInt(13, ids);
            pstmn.setString(14, ciudad);
            concretado = pstmn.executeUpdate();
            cerrarCommit(con);
            return concretado == 1;
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ControlEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            rollback(con);
            return false;
        } finally {
            cerrarPreparaStatement(pstmn);
            cerrarConexion(con);
        }  
    }
    
    public static int devuelveIDEmpresa(int ids) {
        try {
            var elID = 0;
            Connection conec = obtenerConexion();
            PreparedStatement psts = conec.prepareStatement(PARTES);
            psts.setInt(1, ids);
            ResultSet resul = psts.executeQuery();
            if (resul.next()) {
                elID = resul.getInt(1);
            }
            return elID;
        } catch (SQLException ex) {
            Logger.getLogger(ControlEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            return 0;
        } finally {
            cerrarPreparaStatement(pss);
            cerrarResultSet(rest);
            cerrarConexion(con);
        }
    }
    
    public static boolean controlaEmpresa(int ide, boolean status) {
        try {
            var ldt = LocalDateTime.now();
            var tiempo = Timestamp.valueOf(ldt);
            conectadas = obtenerConexion();
            psm = conectadas.prepareStatement(CONTROLEMPRESA);
            psm.setInt(1, ide);
            psm.setBoolean(2, status);
            psm.setTimestamp(3, tiempo);
            int cantidad = psm.executeUpdate();
            cerrarCommit(conectadas);
            return cantidad == 1;
        } catch (SQLException ex) {
            Logger.getLogger(ControlEmpresa.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            return false;
        } finally {
            cerrarPreparaStatement(psm);
            cerrarConexion(conectadas);
        }
    }
    
    @Serial
    private static final long serialVersionUID = -8630890133649335687L;
    private static int concretado;
    private static Connection conex;
    private static Connection con;
    private static Connection conectadas;
    private static PreparedStatement pstmn;
    private static PreparedStatement pss;
    private static PreparedStatement psm;
    private static ResultSet rest;

    private final static String INSERTA = "INSERT INTO datos_representante(nombre_repre, apellido_pat_rep, apellido_mat_rep, "
            + "telefono_rep, celular_rep, email_rep, direccion_rep, foto_rep, codigo_postal, numero_ife, ctrl_curp) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String MUESTRAREPRESENTANTE = "SELECT id_representante, nombre_repre, apellido_pat_rep,"
            + " apellido_mat_rep FROM datos_representante";

    private final static String INSERTAEMPRESA = "INSERT INTO datos_empresa(razon_social,anio_creacion, domicilio_razon, "
            + "codigo_postal, estado, telefono1, telefono2, fax, web, email_razon, nss, logo_empresa, id_representante, ciudad) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String PARTES = "SELECT id_empresa FROM datos_empresa WHERE id_representante= ?";

    private final static String CONTROLEMPRESA = "INSERT INTO control_empresa(id_empresa, estatus_empresa, fecha_activa) "
            + "VALUES(?, ?, ?)";

    private static BufferedImage imagenDelUsuario;
    private static ByteArrayOutputStream byatos;
    private static ByteArrayInputStream inputStream;
}
