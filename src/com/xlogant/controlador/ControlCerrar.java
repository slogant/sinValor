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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.xlogant.conecta.ConectaDB.*;
import static java.sql.Timestamp.*;
import static java.time.LocalDateTime.*;

/**
 * @author oscar
 */
public final class ControlCerrar implements Serializable {

    public static boolean registraSalida(long Unid, long Unrole) {
        try {
            var ldt = now();
            var t = valueOf(ldt);
            laconexion = obtenerConexion();
            pprepa = laconexion.prepareStatement(INSERTANDO);
            pprepa.setLong(1, Unid);
            pprepa.setLong(2, Unrole);
            pprepa.setTimestamp(3, t);
            pprepa.setString(4, CERRANDO);
            int correcto = pprepa.executeUpdate();
            cerrarCommit(laconexion);
            return (correcto == 1);
        } catch (SQLException e) {
            System.out.println("Errores: " + e.getLocalizedMessage());
            System.out.println("El error es: " + e.getSQLState());
            ConectaDB.rollback(laconexion);
            return false;
        } finally {
            cerrarPreparaStatement(pprepa);
            cerrarConexion(laconexion);
        }
    }

    @Serial
    private static final long serialVersionUID = 5986634552120736445L;
    private static Connection laconexion;
    private static PreparedStatement pprepa;
    private static final String CERRANDO = "SALIDA";
    private static final String INSERTANDO = "INSERT INTO ctrol_cerrar(id_usuario, id_role, hora_cerrado, describe) VALUES(?, ?, ?, ?)";
}
