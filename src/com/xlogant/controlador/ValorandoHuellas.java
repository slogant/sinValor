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

import static com.xlogant.conecta.ConectaDB.*;

/**
 * @author oscar
 */
final public class ValorandoHuellas implements Serializable {

    public static EnumMap<DPFPFingerIndex, DPFPTemplate> ObtieneHuellas(int id) {
        try {
            EnumMap<DPFPFingerIndex, DPFPTemplate> template = new EnumMap<>(DPFPFingerIndex.class);
            conectando = obtenerConexion();
            prepareStatemen = conectando.prepareStatement(CONSULTAHUELLA);
            prepareStatemen.setInt(1, id);
            resultado = prepareStatemen.executeQuery();
            while (resultado.next()) {
                String index = resultado.getString(2);
                DPFPFingerIndex indice = DPFPFingerIndex.valueOf(index);
                byte templateBuffer[] = resultado.getBytes(3);
                DPFPTemplate templates = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                template.put(indice, templates);
            }
            return template;
        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getLocalizedMessage());
            return null;
        } finally {
            cerrarPreparaStatement(prepareStatemen);
            cerrarResultSet(resultado);
            cerrarConexion(conectando);
        }
    }

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -4643084159888177855L;
    private static Connection conectando;
    private static PreparedStatement prepareStatemen;
    private static ResultSet resultado;
    private final static String CONSULTAHUELLA = "SELECT ch.id_usuario,ch.identificador_huella, ch.huella_reg FROM control_huellas ch WHERE ch.id_usuario= ?";
}
