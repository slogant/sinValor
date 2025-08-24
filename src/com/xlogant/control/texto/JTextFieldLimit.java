/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.control.texto;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.Serial;
import java.io.Serializable;

/**
 * Un documento de texto para componentes Swing que limita la longitud del texto
 * y opcionalmente convierte la entrada a mayúsculas.
 * <p>
 * Si se intenta insertar un texto que excede el límite (por ejemplo, al pegar),
 * el texto se truncará para ajustarse al espacio restante.
 * <p>
 * Uso:
 * <pre>{@code
 *   JTextField myField = new JTextField();
 *   myField.setDocument(new JTextFieldLimit(10, true)); // Límite de 10 chars, a mayúsculas
 * }</pre>
 *
 * @author oscar
 */
public class JTextFieldLimit extends PlainDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = -6025246524659032521L;

    private final int limit;
    private final boolean toUppercase;

    /**
     * Crea un documento con un límite de caracteres.
     *
     * @param limit El número máximo de caracteres permitidos.
     */
    public JTextFieldLimit(int limit) {
        this(limit, false);
    }

    /**
     * Crea un documento con un límite de caracteres y una opción para convertir a mayúsculas.
     *
     * @param limit       El número máximo de caracteres permitidos.
     * @param toUppercase Si es {@code true}, todo el texto insertado se convertirá a mayúsculas.
     */
    public JTextFieldLimit(int limit, boolean toUppercase) {
        super();
        if (limit < 0) {
            throw new IllegalArgumentException("El límite no puede ser negativo.");
        }
        this.limit = limit;
        this.toUppercase = toUppercase;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        // Si el documento ya está lleno, no se puede insertar más.
        if (getLength() >= limit) {
            return;
        }

        // Calcula cuánto espacio queda.
        int availableSpace = limit - getLength();
        String stringToInsert = str;

        // Si el texto a insertar es más largo que el espacio disponible, trúncalo.
        if (stringToInsert.length() > availableSpace) {
            stringToInsert = stringToInsert.substring(0, availableSpace);
        }

        // Convierte a mayúsculas si es necesario.
        if (toUppercase) {
            stringToInsert = stringToInsert.toUpperCase();
        }

        super.insertString(offset, stringToInsert, attr);
    }
}
