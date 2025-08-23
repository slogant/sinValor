/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.panel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

import static java.lang.System.err;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author oscar
 */
public class PanelControlaImagen extends JPanel implements MouseMotionListener, MouseListener {

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public PanelControlaImagen(BufferedImage unBuffer) {
        color_linea = new Color(255,255,0);
        buffer = unBuffer;
        conImagen = true;
        setSize(new Dimension(anchura, altura));
        setVisible(true);
        //eventos del raton
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override public void paintComponent(Graphics g) {
        //super.paintComponent(g);
         var g2 = (Graphics2D) g;
        //g2.setPaint(paint);
        if (conImagen) {
            otra = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2D = otra.createGraphics();
            g2D.setComposite(AlphaComposite.Src);
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.drawImage(buffer, 0, 0, anchura, altura, null);
            if (otra != null) {
                g2D.setStroke(new BasicStroke(grosor_linea));
                g2D.setColor(color_linea);
                var r2 = new Rectangle2D.Double(clipX, clipY, clipAncho, clipAlto);
                g2D.draw(r2);
                g2.drawImage(otra, 0, 0, anchura, altura, null);
                g2.dispose();
                repaint();
            } else {
                err.println("Vacio");
            }
        }
    }

    //se extrae una subimagen de la imagen original del tamaño der recuadro rojo
    private void recortar() {
        imagenRecorte = ((BufferedImage) otra).getSubimage((int) clipX, (int) clipY, (int) clipAncho, (int) clipAlto);
    }

    /**
     *
     * @return
     */
    public BufferedImage devuelveImagen() {
        imagenRecorte = ((BufferedImage) otra).getSubimage((int) clipX, (int) clipY, (int) clipAncho, (int) clipAlto);
        return imagenRecorte;
    }

    //metodo que guarda la imagen en disco en formato JPG
    public void guardar_imagen(String f) {
        recortar();
        try {
            //se escribe en disco            
            ImageIO.write(imagenRecorte, "jpg", new File(f));
        } catch (IOException e) {

        }
    }

    @Override public void mouseDragged(MouseEvent e) {
        //nuevas coordenadas
        //variables para ek movimiento
        int pos_Marca_new_X = (int) e.getPoint().getX();
        int pos_Marca_new_Y = (int) e.getPoint().getY();

        //System.out.println("new_x=" + Pos_Marca_new_X + " new_Y=" + Pos_Marca_new_Y);
        //se obtiene distancia del movimiento
        int dist_X = pos_Marca_new_X - Pos_Marca_X;
        int dist_Y = pos_Marca_new_Y - Pos_Marca_Y;

        //System.out.println("Dist_x=" + Dist_X + " Dist_Y=" + Dist_Y);
        //se coloca la nueva posicion
        clipX = clipX + dist_X;
        clipY = clipY + dist_Y;
        //System.out.println("clipX=" + clipX + " clipY=" + clipY);

        //evita que se revace los limites de la imagen
        if (clipX < 0) {
            clipX = 0;
        }
        if (clipY < 0) {
            clipY = 0;
        }
        if ((clipX + PanelControlaImagen.clipAncho) > this.getWidth()) {
            clipX = this.getWidth() - PanelControlaImagen.clipAncho;
        }
        if ((clipY + PanelControlaImagen.clipAlto) > this.getHeight()) {
            clipY = this.getHeight() - PanelControlaImagen.clipAlto;
        }
        Pos_Marca_X = Pos_Marca_X + dist_X;
        Pos_Marca_Y = Pos_Marca_Y + dist_Y;
        this.repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {
    }

    @Override public void mouseClicked(MouseEvent e) {

    }

    @Override public void mousePressed(MouseEvent e) {
        Pos_Marca_X = (int) e.getPoint().getX();
        Pos_Marca_Y = (int) e.getPoint().getY();
    }

    @Override public void mouseReleased(MouseEvent e) {
    }

    @Override public void mouseEntered(MouseEvent e) {
    }

    @Override public void mouseExited(MouseEvent e) {
    }

  
    private BufferedImage buffer = null, otra = null, imagenRecorte = null;
    private boolean conImagen = false;
    private final int anchura = panelRepresentante.panelFoto.getWidth();
    private final int altura = panelRepresentante.panelFoto.getHeight();

    //coordenadas y tamaño del recorte
    private double clipX = 0;
    private double clipY = 0;
    private static final double clipAncho = 150;
    private static final double clipAlto = 210;

    private int Pos_Marca_X = 0;
    private int Pos_Marca_Y = 0;

    private Color color_linea = null;
    private final static float grosor_linea = 0.9f;
    private final static float dash1[] = {10.0f};
    private final static BasicStroke dashed
            = new BasicStroke(.5f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);
    private Paint paint;
    @Serial
    private static final long serialVersionUID = -4239501195415884385L;
}