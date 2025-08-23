/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.desktop;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 *
 * @author oscar
 */
final public class FondoDesktop implements Border, Serializable {

    public FondoDesktop() {
        var toolkit = Toolkit.getDefaultToolkit();
        var screenSize = toolkit.getScreenSize();
        xX = screenSize.width;
        yY = screenSize.height;
        try {
            imagen = ImageIO.read(Objects.requireNonNull(getClass().getResource("/com/xlogant/image/11.jpg")));
        } catch (IOException ex) {
            Logger.getLogger(FondoDesktop.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getCause());
        }
    }

    @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        //g.drawImage(imagen, (x + (width - imagen.getWidth()) / 2), (y + (height - imagen.getHeight()) / 2), null);
        g.drawImage(imagen, 0, 0, xX, yY, null);
    }

    @Override public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override public boolean isBorderOpaque() {
        return false;
    }
    @Serial
    private static final long serialVersionUID = 8296722171762155342L;
    private BufferedImage imagen;
    private int xX, yY;
}
