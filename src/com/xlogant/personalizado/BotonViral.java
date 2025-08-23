/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.personalizado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import static javax.swing.BorderFactory.createLineBorder;

/**
 *
 * @author oscar
 */
public class BotonViral extends JButton implements FocusListener, MouseListener {

    /**
     * Constructor de clase
     */
    public BotonViral() {
        super();
        BotonViral.this.setSize(new Dimension(100, 42));
        BotonViral.this.setForeground(new Color(255,255,255));
        BotonViral.this.setBorderPainted(true);
        BotonViral.this.setContentAreaFilled(false);
        BotonViral.this.setOpaque(true);
        BotonViral.this.setBackground(new Color(0,102,102));
        BotonViral.this.setBorder(createLineBorder(new Color(0, 12, 0, 12), 2));
        BotonViral.this.setFocusPainted(false);
        BotonViral.this.addFocusListener(BotonViral.this);
        BotonViral.this.addMouseListener(BotonViral.this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBorder(createLineBorder(new Color(0, 12, 0, 12), 2));
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBorder(createLineBorder(new Color(0, 12, 0, 12), 2));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        BotonViral.this.setBackground(new Color(204,51,0));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        BotonViral.this.setBackground(new Color(102,102,102));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        BotonViral.this.setBackground(new Color(0,153,153));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        BotonViral.this.setBackground(new Color(204,51,0,12));
    }
    @Serial
    private static final long serialVersionUID = -2623180980435879093L;
}
