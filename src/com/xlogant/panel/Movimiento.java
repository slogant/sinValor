/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.Serial;
import javax.swing.JPanel;

/**
 *
 * @author oscar
 */
public class Movimiento extends JPanel {

    public Movimiento() {
       
    }

    @Override
    public void paint(Graphics gc) {
        super.paintComponent(gc);
        var g2d = (Graphics2D) gc.create();
        var t = Toolkit.getDefaultToolkit ();
        var w = getWidth();
        var h = getHeight();
        var imagen = t.getImage ("/home/oscar/Escritorio/javaita.gif");
        var gp = new GradientPaint(
                0, 0, Color.BLACK,
                0, w, Color.black);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        var font = new Font("Tahoma", Font.BOLD + Font.PLAIN, 80);
        g2d.setFont(font);
        g2d.setColor(Color.RED);
        int y = 100;
        g2d.drawString("Marielux", x + 5, y + 5);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Marielux fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", x + 2, y + 2);
        //g2d.drawImage(imagen, x, y, this);
        try {
            x += 10;
            if (x > w) {
                System.out.println(w);
                x = 0;
            }
            revalidate();
            repaint();
            g2d.dispose();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println(e.getCause());
        }

    }

    @Serial
    private static final long serialVersionUID = -2837011301335573633L;
    private int x = 0;

}
