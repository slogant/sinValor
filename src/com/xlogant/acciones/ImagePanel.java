/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

/**
 *
 * @author oscar
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import static java.time.LocalDateTime.*;

/**
 * @author oscar
 *
 */
final public class ImagePanel extends JPanel {

    public ImagePanel() {
        super();
    }

    public void setImagen(BufferedImage mImagen) {
        this.mImagen = mImagen;
    }

    public BufferedImage getImagen() {
        return mImagen;
    }

    public boolean guardarImagenEnArchivo(String pPath) {
        if (mImagen == null) {
            return false;
        }
        try (var s = new FileOutputStream(pPath)) {
            ImageIO.write(mImagen, "png", s);
            s.close();
            return true; // ok
        } catch (IOException e) {
            // TODO: Add catch code
            System.out.println("Error: " + e.getLocalizedMessage());
             return false;
        }
    }

    public boolean guardarDirecto() {
        /*
         //Verde
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
         //convert to green image
         for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
         int p = mImagen.getRGB(x, y);

         int a = (p >> 24) & 0xff;
         int g = (p >> 8) & 0xff;

         //set new RGB
         p = (a << 24) | (0 << 16) | (g << 8) | 0;

         mImagen.setRGB(x, y, p);

         }
         }*/

 /*
         //Azul
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
        
         //convert to blue image
         for(int y = 0; y < height; y++){
         for(int x = 0; x < width; x++){
         int p = mImagen.getRGB(x,y);
                
         int a = (p>>24)&0xff;
         int b = p&0xff;
                
         //set new RGB
         p = (a<<24) | (0<<16) | (0<<8) | b;
                
         mImagen.setRGB(x, y, p);
         }
         }*/
 /*
         //convertir a rojo la imagen
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
         for(int y = 0; y < height; y++){
         for(int x = 0; x < width; x++){
         int p = mImagen.getRGB(x,y);
                
         int a = (p>>24)&0xff;
         int r = (p>>16)&0xff;
                
         //set new RGB
         p = (a<<24) | (r<<16) | (0<<8) | 0;
                
         mImagen.setRGB(x, y, p);
         }
         }*/
 /*
         //get image width and height
         //Imagen a negativo
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
         //convert to negative
         for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
         int p = mImagen.getRGB(x, y);
         int a = (p >> 24) & 0xff;
         int r = (p >> 16) & 0xff;
         int g = (p >> 8) & 0xff;
         int b = p & 0xff;
         //subtract RGB from 255
         r = 255 - r;
         g = 255 - g;
         b = 255 - b;
         //set new RGB value
         p = (a << 24) | (r << 16) | (g << 8) | b;
         mImagen.setRGB(x, y, p);
         }
         }*/
 /*
         //Espejo
         //get source image dimension
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
         //BufferedImage for mirror image
         BufferedImage mimg = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_ARGB);
         //create mirror image pixel by pixel
         for (int y = 0; y < height; y++) {
         for (int lx = 0, rx = width * 2 - 1; lx < width; lx++, rx--) {
         //lx starts from the left side of the image
         //rx starts from the right side of the image
         //get source pixel value
         int p = mImagen.getRGB(lx, y);
         //set mirror image pixel value - both left and right
         mImagen.setRGB(lx, y, p);
         mImagen.setRGB(rx, y, p);
         }
         }*/

 /*
         //image dimension
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();
         //create buffered image object img
         BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
         //file object
         File f = null;
         //create random image pixel by pixel
         for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
         int a = (int) (Math.random() * 256); //alpha
         int r = (int) (Math.random() * 256); //red
         int g = (int) (Math.random() * 256); //green
         int b = (int) (Math.random() * 256); //blue

         int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

         mImagen.setRGB(x, y, p);
         }
         }*/
 /*
         //get width and height of the image
         int width = mImagen.getWidth();
         int height = mImagen.getHeight();

         //convert to sepia
         for(int y = 0; y < height; y++){
         for(int x = 0; x < width; x++){
         int p = mImagen.getRGB(x,y);

         int a = (p>>24)&0xff;
         int r = (p>>16)&0xff;
         int g = (p>>8)&0xff;
         int b = p&0xff;

         //calculate tr, tg, tb
         int tr = (int)(0.393*r + 0.769*g + 0.189*b);
         int tg = (int)(0.349*r + 0.686*g + 0.168*b);
         int tb = (int)(0.272*r + 0.534*g + 0.131*b);

         //check condition
         if(tr > 255){
         r = 255;
         }else{
         r = tr;
         }

         if(tg > 255){
         g = 255;
         }else{
         g = tg;
         }

         if(tb > 255){
         b = 255;
         }else{
         b = tb;
         }

         //set new RGB value
         p = (a<<24) | (r<<16) | (g<<8) | b;

         mImagen.setRGB(x, y, p);
         }
         }*/
        if (mImagen == null) {
            return false;
        }
        /*try (FileOutputStream s = new FileOutputStream(direc)) {
               
         s.close();
         }*/
        try {
            var direc = System.getProperty("user.home") + "/Documentos/test/";
            var local = now();
            ImageIO.write(mImagen, "png", new File(direc + " " + local + "." + "png"));
            System.out.println("Imagen guradada correctamente..................");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    
    @Override public void paint(Graphics g) {
        super.paint(g);
        if (mImagen != null) {
            try {
                g.drawImage(mImagen, 0, 0, this.getWidth(), this.getHeight(), null);
            } catch (Exception e) {
                // TODO: Add catch code
                System.out.println("Error: " + e.getLocalizedMessage());
            }
        }
    }
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -4102449963762958616L;
    private BufferedImage mImagen = null;
}
