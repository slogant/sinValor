/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.encripta;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidad para generar resúmenes criptográficos (hashes)
 * utilizando algoritmos de la familia SHA y otros.
 * <p>
 * Esta clase no puede ser instanciada.
 *
 * @author slogant
 * @author slogant (refactored by Gemini)
 */
public final class ShaUtils {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private ShaUtils() {
    }

    /**
     * Genera un hash SHA-256 para una cadena de texto.
     *
     * @param input la cadena de texto a procesar.
     * @return el hash SHA-256 en formato de cadena hexadecimal.
     */
    public static String sha256(final String input) {
        byte[] digest = digest(input.getBytes(UTF_8), "SHA-256");
        return bytesToHex(digest);
    }

    /**
     * Genera un hash SHA-512 para una cadena de texto.
     *
     * @param input la cadena de texto a procesar.
     * @return el hash SHA-512 en formato de cadena hexadecimal.
     */
    public static String sha512(final String input) {
        byte[] digest = digest(input.getBytes(UTF_8), "SHA-512");
        return bytesToHex(digest);
    }

    /**
     * Calcula el resumen (digest) de un arreglo de bytes usando el algoritmo especificado.
     *
     * @param input     el arreglo de bytes de entrada.
     * @param algorithm el nombre del algoritmo de resumen (ej. "SHA-256", "MD5").
     * @return el arreglo de bytes que representa el hash.
     * @throws IllegalArgumentException si el algoritmo no es válido o no está disponible.
     */
    public static byte[] digest(final byte[] input, final String algorithm) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return md.digest(input);
    }

    /**
     * Convierte un arreglo de bytes a su representación en cadena hexadecimal.
     *
     * @param bytes el arreglo de bytes a convertir.
     * @return una cadena de texto en formato hexadecimal.
     */
    public static String bytesToHex(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
