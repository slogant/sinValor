/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.customize;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serial;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author oscar
 */
final public class MiCombo extends JLabel implements ListCellRenderer<Object> {

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MiCombo() {
        setOpaque(true);
        setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(0, 0, 245));
    }

    @Override public Component getListCellRendererComponent(JList<? extends Object> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        return this;
    }
    @Serial
    private static final long serialVersionUID = 4846973213177777108L;
} 
