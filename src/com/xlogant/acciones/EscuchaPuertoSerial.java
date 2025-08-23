/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

import com.xlogant.conecta.ControlaPuerto;
import com.xlogant.panel.PanelCrearUsuario;
import com.xlogant.principal.CentroPrincipal;
import java.awt.Color;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showInternalMessageDialog;
import javax.swing.JTextField;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author oscar
 */
final public class EscuchaPuertoSerial implements SerialPortEventListener {
    
    public EscuchaPuertoSerial(JTextField texto, JButton boton, 
            JComboBox<String> elcombox) {
        text = texto;
        btns = boton;
        comb = elcombox;
    }

    static public List<String> MuestraPuertos() {
        String[] portNames = SerialPortList.getPortNames();
        List<String> listaPuertos;
        return listaPuertos = new LinkedList<>(Arrays.asList(portNames));
    }

    static public SerialPort AbrirPuerto(String puertox) {
        if (puertox.isEmpty()) {
            System.out.println("Selecciona el puerto");
            return null;
        } else {
            try {
                elPuerto = new SerialPort(puertox);
                if (!elPuerto.isOpened()) {
                    elPuerto.openPort() ;//Abre el puerto serial
                    elPuerto.setParams(SerialPort.BAUDRATE_9600, //Configuración del puerto serial
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    System.out.println("El puerto se abrio");
                    showInternalMessageDialog(CentroPrincipal.jDesktopPane1, "Se abrio el puerto para lectura", "Monitor", INFORMATION_MESSAGE);
                } else {
                    System.out.println("El puerto ya esta abierto....");
                    return null;
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.printf("Error del puerto: %s%n-> ", ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
                return null;
            }
            return elPuerto;
        }
    }

    static public void cerrarPuerto(SerialPort el) {
        elPuerto = el;
        if (elPuerto.isOpened()) {
            try {
                elPuerto.closePort();
                System.out.println("El puerto se ha cerrado");
            } catch (SerialPortException ex) {
                Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error del puerto: -> " + ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
                elPuerto = null;
                System.exit(0);
            }
        } else {
            System.out.println("No se logro cerrar el puerto");
        }
    }

    @Override public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && (event.getEventValue() >= 17)) {
            try {
                var ldt = LocalDateTime.now();
                var tiempo = Timestamp.valueOf(ldt);
                var n = new StringBuilder();
                var sb = new StringBuilder();//Variable para almacenar String del puerto
                sb.append(elPuerto.readString(event.getEventValue()));//Almaceno el String que me envia el puerto
                String se = (String) sb.substring(0, sb.length() - 1);//Le quito un caracter que no nececito
                System.out.println(se);
                var palabras = se.split("^[A-Za-z_0-9]*");//Elimino caracteres especiales
                for (String t : palabras) {
                    System.out.println(t);
                    n.append(t);//LO almaceno en una nueva variable
                }
                var yo = n.toString().trim();//Covierto la variable n a String
                text.requestFocus();
                text.setText(yo);
                text.setForeground(new Color(45, 20, 245));
                if (!yo.isEmpty()) {
                    sb.append("");
                    sb.setLength(0);
                    btns.setEnabled(false);
                    comb.setEnabled(false);
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(PanelCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("error: -> " + ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
            }
        }
    }

    private static SerialPort elPuerto;
    private static JTextField text;
    private static JButton btns;
    private static JComboBox<String> comb;

}
