/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.conecta;

import com.xlogant.principal.CentroPrincipal;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import static com.xlogant.conecta.ConectaDB.obtenerConexion;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTextField;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import static javax.swing.JOptionPane.showInternalMessageDialog;

/**
 *
 * @author oscar
 */
final public class ControlaPuerto implements SerialPortEventListener {

    public ControlaPuerto(JTextField txt, JButton btn, JComboBox<String> cmb, JLabel eti) {
        texto = txt;
        boton = btn;
        combo = cmb;
        etiqueta = eti;
    }

    static public List<String> MuestraPuertos() {
        String[] portNames = SerialPortList.getPortNames();
        return new LinkedList<>(Arrays.asList(portNames));
    }

    static public SerialPort abrePuerto(String puerto) {
        if (puerto.isEmpty()) {
            System.out.println("Selecciona el puerto");
            return null;
        } else {
            try {
                elPuerto = new SerialPort(puerto);
                if (!elPuerto.isOpened()) {
                    elPuerto.openPort();//Abre el puerto serial
                    elPuerto.setParams(SerialPort.BAUDRATE_9600, //Configuración del puerto serial
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } else {
                    System.out.println("El puerto ya esta abierto....");
                    elPuerto.closePort();
                    return null;
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error del puerto: -> " + ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
                try {
                    elPuerto.closePort();
                } catch (SerialPortException ex1) {
                    Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex1);
                    showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex1.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
                }
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
                //Timestamp tiempo = Timestamp.valueOf(ldt);
                cone = obtenerConexion();
                pstm = cone.prepareStatement(SENTENCIA);
                Runnable task2 = () -> {
                    try {
                        //Cadena a comparar
                        var n = new StringBuilder();
                        var sb = new StringBuilder();//Variable para almacenar String del puerto
                        sb.append(elPuerto.readString(event.getEventValue()));//Almaceno el String que me envia el puerto
                        var se = (String) sb.substring(0, sb.length() - 1);//Le quito un caracter que no nececito
                        System.out.println(se);
                        var palabras = se.split("^[A-Za-z_0-9]*");//Elimino caracteres especiales
                        for (String t : palabras) {
                            System.out.println(t);
                            n.append(t);//LO almaceno en una nueva variable
                        }
                        var yo = n.toString().trim();//Covierto la variable n a String
                        texto.requestFocus();
                        texto.setText(yo);
                        texto.setForeground(new Color(45, 20, 245));
                        System.out.println(yo.length());//Checo que se ha igual de tamaño
                        pstm.setString(1, yo);
                        res = pstm.executeQuery();
                        while (res.next()) {
                            valor = res.getString(1);
                            System.out.println("La rfid es:--------------------->>>>>>>>>>>>>>>>>> " + valor);
                            if (yo.equals(valor)) {
                                System.out.println("El campo es:------------------------------------------->>>> " + valor);
                                texto.setForeground(Color.RED);
                                etiqueta.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/com/xlogant/image/tache.png"))));
                                texto.requestFocus();
                                Thread.sleep(2000);
                                texto.setText("");
                                etiqueta.setIcon(null);
                            } else {
                                texto.setForeground(Color.ORANGE);
                                etiqueta.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/com/xlogant/image/palomita.png"))));
                                texto.requestFocus();
                                Thread.sleep(2000);
                                texto.setText("");
                                etiqueta.setIcon(null);
                            }
                        }
                        //System.out.println("Valor encontrado " + valor);
                       /* if(!yo.equals(ya)){
                                 boolean rr = (!yo.equals(ya));
                                System.out.println("El valor es: --------> " + rr);
                            } else {
                                System.out.println("No esta--------------------------------------------------------------------");
                            }*/
                        Thread.sleep(1000);
                        sb.append("");
                        sb.setLength(0);
                        texto.setEditable(false);
                        combo.setEnabled(false);
                        boton.setEnabled(false);
                    } catch (SerialPortException | InterruptedException | SQLException ex) {
                        Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("error: -> " + ex.getLocalizedMessage());
                    } finally {
                        if (cone != null) {
                            ConectaDB.cerrarConexion(cone);
                            boolean ty = tarea.isAlive();
                            if (tarea.isAlive()) {
                                System.out.println("Tarea terminada..." + ty);
                                tarea.interrupt();
                                if (tarea.isInterrupted()) {
                                    System.out.println("La tarea se ha terminado...");
                                }
                            }
                        }
                    }
                };
                tarea = new Thread(task2);
                tarea.start();
            } catch (SQLException ex) {
                Logger.getLogger(ControlaPuerto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("El error es: -> " + ex.getLocalizedMessage());
            }

            /*try {

             LocalDateTime ldt = LocalDateTime.now();
             Timestamp tiempo = Timestamp.valueOf(ldt);

             StringBuilder n = new StringBuilder();
             StringBuilder sb = new StringBuilder();//Variable para almacenar String del puerto
             sb.append(elPuerto.readString(event.getEventValue()));//Almaceno el String que me envia el puerto
             String se = (String) sb.substring(0, sb.length() - 1);//Le quito un caracter que no nececito
             System.out.println(se);
             String[] palabras = se.split("^[A-Za-z_0-9]*");//Elimino caracteres especiales
             for (String t : palabras) {
             System.out.println(t);
             n.append(t);//LO almaceno en una nueva variable
             }
             String yo = n.toString().trim();//Covierto la variable n a String
             texto.requestFocus();
             texto.setText(yo);
             texto.setForeground(new Color(45, 20, 245));
             System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrr------------------___> " + yo);
             if (!yo.isEmpty()) {
             sb.append("");
             sb.setLength(0);
             texto.setEditable(false);
             combo.setEnabled(false);
             boton.setEnabled(false);
             }

             } catch (SerialPortException ex) {
             Logger.getLogger(PanelCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("error: -> " + ex.getLocalizedMessage());
             showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", ERROR_MESSAGE);
             }
             }*/
        }
    }

    private static SerialPort elPuerto;
    private static JLabel etiqueta;
    private static JTextField texto;
    private static JButton boton;
    private static JComboBox<String> combo;
    private Connection con, cone;
    private PreparedStatement pstm, ps;
    private ResultSet res;
    private Thread tarea;
    private final static String SENTENCIA = "SELECT rfid FROM datos_usuarios WHERE rfid= ?";
    private String valor;
}
