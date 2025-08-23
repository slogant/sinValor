package com.xlogant.panel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serial;
import javax.swing.JPanel;

/**
 *
 * @author Oscar
 */
final public class JPanelGradient extends JPanel {
    
    public JPanelGradient() {
    }

    @Override protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g.create();
        var clip = g2.getClipBounds();
        var x = getWidth();
        var y = getHeight();
        g2.setPaint(new GradientPaint(0.0f, 0.0f, color1.darker(),
                0.0f, getHeight(), color2.darker()));
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

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
    private static final long serialVersionUID = 8566914509152463093L;
    private Color color1 = new Color(0, 0, 25);
    private Color color2 = new Color(245, 245, 245);
    private final float x1 = 0;
    private final float y1 = 0;
    private final float x2 = getWidth();
    private final float y2 = getHeight();

}
