/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.efecto;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.JComponent;
import javax.swing.JFrame;

import static java.util.stream.IntStream.*;

/**
 *
 * @author slogant
 */
public class Dissolver extends JComponent implements Runnable {

    public Dissolver() {
    }

    public void dissolveExit(JFrame frame) {
        try {
            var robot = new Robot();
            var frame_rect = frame.getBounds();
            var frame_buffer = robot.createScreenCapture(frame_rect);
            frame.setVisible(true);
            var screensize = Toolkit.getDefaultToolkit().getScreenSize();
            var screen_rect = new Rectangle(0, 0, screensize.width, screensize.height);
            var screen_buffer = robot.createScreenCapture(screen_rect);
            fullscreen = new Window(frame);
            fullscreen.setSize(screensize);
            fullscreen.add(this);
            this.setSize(screensize);
            fullscreen.setVisible(true);
            new Thread(this).start();
        } catch (AWTException ex) {
            Logger.getLogger(Dissolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            count = 0;
            Thread.sleep(100);
            range(0, 20).forEach(x -> {
                count = x;
                fullscreen.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Dissolver.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (InterruptedException e) {
            System.exit(0);
        }
    }

    private Window fullscreen;
    private int count;
}
