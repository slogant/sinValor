/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.personalizado;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serial;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import static java.awt.RenderingHints.*;

/**
 *
 * @author oscar
 */
public class TextViral extends JTextField implements FocusListener {

    /**
     * Constructor de clase
     */
    public TextViral() {
        super();
        borderColor = new Color(51,51,255);
        TextViral.this.setText("");
        TextViral.this.setForeground(new Color(51,51,255));
        TextViral.this.setPreferredSize(new Dimension(200, 36));
        TextViral.this.setVisible(true);
        TextViral.this.setFont(new Font("Dialog", Font.PLAIN, 12));
        TextViral.this.setBorder(new EmptyBorder(0, 12, 0, 12));
        TextViral.this.setCaretColor(new Color(248, 110, 1));
        TextViral.this.setSelectionColor(new Color(153,0,0));
        TextViral.this.setSelectedTextColor(new Color(162, 183, 188));
        TextViral.this.putClientProperty("caretWidth", 4);
        TextViral.this.setOpaque(false);
        TextViral.this.addFocusListener(TextViral.this);
    }

    @Override
    public void paintComponent(Graphics g) {

        var g2 = (Graphics2D) g;
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        var fig = new Polygon();
        fig.addPoint(0, 0);
        fig.addPoint(getWidth() - 3, 0);
        fig.addPoint(getWidth() - 3, getHeight() - 18);
        fig.addPoint(getWidth() - 18, getHeight() - 3);
        fig.addPoint(0, getHeight() - 3);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fill(fig);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(3));
        g2.draw(fig);

        super.paintComponent(g);
    }

    @Override
    public void focusGained(FocusEvent e) {
        borderColor = new Color(248, 110, 1);
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        borderColor = new Color(162, 183, 188);
        repaint();
    }

    @Serial
    private static final long serialVersionUID = -1204605754076663365L;
    private Color borderColor;

}
