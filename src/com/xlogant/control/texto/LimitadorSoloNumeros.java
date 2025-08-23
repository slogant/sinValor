/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.control.texto;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.Serial;
import java.io.Serializable;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 *
 * @author oscar
 */
public class LimitadorSoloNumeros extends PlainDocument implements Serializable {

    public LimitadorSoloNumeros(JTextField unTexto, int unLimite) {
        miTexto = unTexto;
        limite = unLimite;
    }

    @Override
    @SuppressWarnings("SillyAssignment")
    public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
        arg1 = arg1;
        for (int i = 0; i < arg1.length(); i++) {
            if ((miTexto.getText().length() + arg1.length()) > limite) {
                getDefaultToolkit().beep();
                return;
            } else if (!Character.isDigit(arg1.charAt(i)) && !Character.isSpaceChar(arg1.charAt(i))) {
                getDefaultToolkit().beep();
                return;
            } else if (Character.isSpaceChar(arg1.charAt(i))) {
                getDefaultToolkit().beep();
                return;
            }
        }
        super.insertString(arg0, arg1, arg2);
    }
    @Serial
    private static final long serialVersionUID = 1476100382238016359L;
    private final int limite;
    private final JTextField miTexto;
}
