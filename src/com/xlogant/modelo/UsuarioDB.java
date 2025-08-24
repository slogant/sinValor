package com.xlogant.modelo;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa un objeto de transferencia de datos (DTO) para un usuario.
 * <p>
 * Esta clase es un {@link Record} de Java, lo que la hace inmutable y
 * proporciona automáticamente constructores, getters, equals(), hashCode() y toString().
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public record UsuarioDB(
        long idUsuario,
        String nombreUsuario,
        String claveUsuario,
        boolean estatusUsuario,
        long idRole
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -8327757545315661753L;
}
