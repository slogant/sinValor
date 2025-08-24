package com.xlogant.acciones;

/**
 * Interfaz para escuchar eventos de datos recibidos desde un puerto serial.
 */
public interface SerialDataListener {
    /**
     * Se invoca cuando se reciben nuevos datos del puerto serial.
     * @param data Los datos recibidos como una cadena de texto.
     */
    void onDataReceived(String data);

    /**
     * Se invoca cuando ocurre un error en la comunicación del puerto serial.
     * @param errorMessage El mensaje de error.
     */
    void onError(String errorMessage);
}