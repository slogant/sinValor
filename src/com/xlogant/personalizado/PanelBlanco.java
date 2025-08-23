/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.personalizado;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serial;

/**
 *
 * @author oscar
 */
public class PanelBlanco extends javax.swing.JPanel {
    
    public PanelBlanco() {
        color1 = new Color(255, 255, 255);
        color2 = new Color(0, 0, 125);
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        var clip = g2.getClipBounds();
        var x = getWidth();
        var y = getHeight();
        g2.setPaint(new GradientPaint(0.0f, 0.0f, color1.darker(), 0.0f, getHeight(), color2.darker()));
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

//Métodos set y get que nos permiten modificar los colores
    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    @Serial
    private static final long serialVersionUID = -760836813023186953L;
    private Color color1;
    private Color color2;

}
