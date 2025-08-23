/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.encripta;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.toHexString;

/**
 *
 * @author oscar
 */
final public class EncriptaGenerador implements Serializable {

    /**
     * *
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     *
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de digest
     */
    static private String toHexadecimal(byte[] digest) {
        var hash = "";
        for (var aux : digest) {
            var b = aux & 0xff;
            if (toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += toHexString(b);
        }
        return hash;
    }

    /**
     * *
     * Encripta una cadena mediante algoritmo de resumen de mensaje.
     *
     * @param cadena texto a encriptar
     * @param tipoAlgoritmo
     * @return mensaje encriptado
     */
    static public String getHash(String cadena, int tipoAlgoritmo) {
        try {
            byte[] digest;
            var buffer = cadena.getBytes();
            var messageDigest = MessageDigest.getInstance(algoritmos[tipoAlgoritmo]);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
            return toHexadecimal(digest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncriptaGenerador.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
    }

    public static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512 
            var md = MessageDigest.getInstance("SHA-512");

            // digest() method is called 
            // to calculate message digest of the input string 
            // returned as array of byte 
            var messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation 
            var no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value 
            var hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit 
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText 
            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final long serialVersionUID = -3652912919420371454L;
    static final private String[] algoritmos = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"};
}
