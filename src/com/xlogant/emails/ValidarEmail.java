/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.emails;

import java.util.regex.Pattern;

/**
 * Clase de utilidad para validar direcciones de correo electrónico.
 * <p>
 * Esta clase no puede ser instanciada y proporciona un método estático
 * para la validación.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public final class ValidarEmail {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * El patrón de expresión regular pre-compilado para la validación de emails.
     * Se compila una sola vez para mejorar el rendimiento.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private ValidarEmail() {}

    /**
     * Valida una dirección de correo electrónico usando una expresión regular.
     *
     * @param email el correo electrónico a validar.
     * @return {@code true} si el email es válido, {@code false} en caso contrario.
     */
    public static boolean validaEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
