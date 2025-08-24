/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.encripta;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidad para generar resúmenes (hashes) de cadenas de texto
 * utilizando diversos algoritmos criptográficos.
 * <p>
 * Esta clase no puede ser instanciada.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
final public class EncriptaGenerador implements Serializable {

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private EncriptaGenerador() {
    }

    /**
     * Convierte un arreglo de bytes a su representación en cadena hexadecimal.
     * Utiliza un StringBuilder para un rendimiento óptimo.
     *
     * @param digest el arreglo de bytes a convertir.
     * @return una cadena de texto en formato hexadecimal.
     */
    private static String toHexadecimal(byte[] digest) {
        StringBuilder hexString = new StringBuilder(2 * digest.length);
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Encripta (genera un hash) una cadena de texto usando un algoritmo de la lista predefinida.
     *
     * @param cadena        el texto a encriptar.
     * @param tipoAlgoritmo el índice del algoritmo a usar del array {@code algoritmos}.
     * @return el mensaje encriptado en formato hexadecimal.
     * @throws IllegalStateException si el algoritmo de hash solicitado no se encuentra,
     *                               lo que indica un error de programación.
     * @throws IllegalArgumentException si la cadena de entrada es nula o el tipo de algoritmo es inválido.
     */
    public static String getHash(String cadena, int tipoAlgoritmo) {
        if (cadena == null) {
            throw new IllegalArgumentException("La cadena de entrada no puede ser nula.");
        }
        if (tipoAlgoritmo < 0 || tipoAlgoritmo >= algoritmos.length) {
            throw new IllegalArgumentException("El tipo de algoritmo es inválido: " + tipoAlgoritmo);
        }

        try {
            // Es una buena práctica especificar siempre el charset.
            byte[] buffer = cadena.getBytes(StandardCharsets.UTF_8);
            MessageDigest messageDigest = MessageDigest.getInstance(algoritmos[tipoAlgoritmo]);
            byte[] digest = messageDigest.digest(buffer);
            return toHexadecimal(digest);
        } catch (NoSuchAlgorithmException ex) {
            // Esto no debería ocurrir si los nombres en el array son correctos.
            // Lanzar una excepción en tiempo de ejecución es apropiado aquí.
            throw new IllegalStateException("Algoritmo de hash no encontrado: " + algoritmos[tipoAlgoritmo], ex);
        }
    }

    private static final long serialVersionUID = -3652912919420371454L;
    private static final String[] algoritmos = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"};
}
