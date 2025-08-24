/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestiona la escucha de un puerto serial específico y notifica a un listener
 * cuando se reciben datos o ocurren errores.
 * Esta clase implementa AutoCloseable para un manejo de recursos seguro.
 *
 * @author oscar
 */
public class EscuchaPuertoSerial implements SerialPortEventListener, AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(EscuchaPuertoSerial.class.getName());
    private final SerialPort serialPort;
    private final SerialDataListener dataListener;
    private final StringBuilder buffer = new StringBuilder();

    /**
     * Construye un listener para un puerto serial.
     *
     * @param portName El nombre del puerto a abrir (ej. "COM3" o "/dev/ttyUSB0").
     * @param listener El listener que recibirá los datos y errores.
     * @throws SerialPortException si el puerto no puede ser abierto o configurado.
     */
    public EscuchaPuertoSerial(String portName, SerialDataListener listener) throws SerialPortException {
        if (portName == null || portName.isBlank()) {
            throw new IllegalArgumentException("El nombre del puerto no puede ser nulo o vacío.");
        }
        if (listener == null) {
            throw new IllegalArgumentException("El listener no puede ser nulo.");
        }

        this.dataListener = listener;
        this.serialPort = new SerialPort(portName);

        serialPort.openPort();
        serialPort.setParams(
                SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );
        serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
        LOGGER.info("Puerto " + portName + " abierto y escuchando.");
    }

    /**
     * Método estático de utilidad para obtener la lista de puertos seriales disponibles en el sistema.
     * @return Una lista de nombres de puertos.
     */
    public static List<String> getAvailablePorts() {
        return Arrays.asList(SerialPortList.getPortNames());
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        // Se asegura de que el evento sea de recepción de datos y que haya datos disponibles.
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                // Lee los bytes disponibles del puerto.
                byte[] receivedBytes = serialPort.readBytes(event.getEventValue());
                // Convierte los bytes a String y los añade al buffer.
                buffer.append(new String(receivedBytes));

                // Procesa el buffer si contiene un terminador (ej. nueva línea).
                // Esto es más robusto que procesar por número de bytes.
                // Asumimos que cada lectura completa termina con '\n'.
                int lineEndIndex;
                while ((lineEndIndex = buffer.indexOf("\n")) >= 0) {
                    // Extrae la línea completa.
                    String line = buffer.substring(0, lineEndIndex).trim();
                    // Elimina la línea procesada del buffer.
                    buffer.delete(0, lineEndIndex + 1);

                    if (!line.isEmpty()) {
                        // Limpia la cadena de caracteres no deseados.
                        // Este regex mantiene solo letras, números y guiones bajos.
                        final String cleanedData = line.replaceAll("[^A-Za-z0-9_]", "");

                        // Notifica al listener en el hilo de la UI (EDT) para seguridad.
                        SwingUtilities.invokeLater(() -> dataListener.onDataReceived(cleanedData));
                    }
                }
            } catch (SerialPortException ex) {
                LOGGER.log(Level.SEVERE, "Error al leer del puerto serial.", ex);
                // Notifica al listener del error, también en el hilo de la UI.
                SwingUtilities.invokeLater(() -> dataListener.onError("Error de lectura: " + ex.getMessage()));
            }
        }
    }

    /**
     * Cierra el puerto serial y libera los recursos.
     * Este método es invocado automáticamente cuando se usa en un bloque try-with-resources.
     */
    @Override
    public void close() {
        if (serialPort != null && serialPort.isOpened()) {
            try {
                serialPort.removeEventListener();
                serialPort.closePort();
                LOGGER.info("Puerto " + serialPort.getPortName() + " cerrado correctamente.");
            } catch (SerialPortException ex) {
                LOGGER.log(Level.SEVERE, "Error al cerrar el puerto serial.", ex);
                // Opcional: notificar al listener del error de cierre.
                SwingUtilities.invokeLater(() -> dataListener.onError("Error al cerrar el puerto: " + ex.getMessage()));
            }
        }
    }
}
