/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.customize;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 *
 * @author oscar
 */
public final class MiComboEditor extends BasicComboBoxEditor {

    public MiComboEditor() {
        label = new JLabel();
        panel = new JPanel();
        label.setOpaque(false);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setBackground(new Color(255, 255, 255));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.add(label);
        panel.setBackground(Color.GREEN);
    }

    @Override
    public Component getEditorComponent() {
        return this.panel;
    }

    @Override
    public Object getItem() {
        return "[" + this.selectedItem.toString() + "]";
    }

    @Override
    public void setItem(Object item) {
        this.selectedItem = item;
        label.setText(item.toString());
    }
    private final JLabel label;
    private final JPanel panel;
    private Object selectedItem;
}
