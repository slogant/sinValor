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
public class PanelControlLogo extends JPanel implements MouseMotionListener, MouseListener {

    public PanelControlLogo(BufferedImage unBuffered) {
        color_lineas = new Color(255, 255, 0);
        buffers = unBuffered;
        conImagen = true;
        setSize(new Dimension(anchuras, alturas));
        setVisible(true);
        //eventos del raton
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        var g2dx = (Graphics2D) g;
        //g2.setPaint(paint);
        if (conImagen) {
            otras = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2Ds = otras.createGraphics();
            g2Ds.setComposite(AlphaComposite.Src);
            g2Ds.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2Ds.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2Ds.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2Ds.drawImage(buffers, 0, 0, anchuras, alturas, null);
            if (otras != null) {
                g2Ds.setStroke(new BasicStroke(grosor_linea));
                g2Ds.setColor(color_lineas);
                var r2d = new Rectangle2D.Double(clipXx, clipYy, clipAnchos, clipAltos);
                g2Ds.draw(r2d);
                g2dx.drawImage(otras, 0, 0, anchuras, alturas, null);
                g2dx.dispose();
                repaint();
            } else {
                err.println("Vacio");
            }
        }
    }

    //se extrae una subimagen de la imagen original del tamaño der recuadro rojo
    private void recortars() {
        imagenRecortes = ((BufferedImage) otras).getSubimage((int) clipXx, (int) clipYy, (int) clipAnchos, (int) clipAltos);
    }

    /**
     *
     * @return
     */
    public BufferedImage devuelveImagenes() {
        imagenRecortes = ((BufferedImage) otras).getSubimage((int) clipXx, (int) clipYy, (int) clipAnchos, (int) clipAltos);
        return imagenRecortes;
    }

    //metodo que guarda la imagen en disco en formato JPG
    public void guardar_imagenes(String f) {
        recortars();
        try {
            //se escribe en disco            
            ImageIO.write(imagenRecortes, "jpg", new File(f));
        } catch (IOException e) {

        }
    }

    @Override public void mouseDragged(MouseEvent e) {
        //nuevas coordenadas
        //variables para ek movimiento
        int pos_Marca_new_Xx = (int) e.getPoint().getX();
        int pos_Marca_new_Yy = (int) e.getPoint().getY();

        //System.out.println("new_x=" + Pos_Marca_new_X + " new_Y=" + Pos_Marca_new_Y);
        //se obtiene distancia del movimiento
        int dist_Xx = pos_Marca_new_Xx - Pos_Marca_Xx;
        int dist_Yy = pos_Marca_new_Yy - Pos_Marca_Yy;

        //System.out.println("Dist_x=" + Dist_X + " Dist_Y=" + Dist_Y);
        //se coloca la nueva posicion
        clipXx = clipXx + dist_Xx;
        clipYy = clipYy + dist_Yy;
        //System.out.println("clipX=" + clipX + " clipY=" + clipY);

        //evita que se revace los limites de la imagen
        if (clipXx < 0) {
            clipXx = 0;
        }
        if (clipYy < 0) {
            clipYy = 0;
        }
        if ((clipXx + PanelControlLogo.clipAnchos) > this.getWidth()) {
            clipXx = this.getWidth() - PanelControlLogo.clipAnchos;
        }
        if ((clipYy + PanelControlLogo.clipAltos) > this.getHeight()) {
            clipYy = this.getHeight() - PanelControlLogo.clipAltos;
        }
        Pos_Marca_Xx = Pos_Marca_Xx + dist_Xx;
        Pos_Marca_Yy = Pos_Marca_Yy + dist_Yy;
        this.repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {
    }

    @Override public void mouseClicked(MouseEvent e) {
    }

    @Override public void mousePressed(MouseEvent e) {
        Pos_Marca_Xx = (int) e.getPoint().getX();
        Pos_Marca_Yy = (int) e.getPoint().getY();
    }

    @Override public void mouseReleased(MouseEvent e) {
    }

    @Override public void mouseEntered(MouseEvent e) {
    }

    @Override public void mouseExited(MouseEvent e) {
    }

    private BufferedImage buffers = null, otras = null, imagenRecortes = null;
    private boolean conImagen = false;
    private final int anchuras = panelRepresentante.paneldellogo.getWidth();
    private final int alturas = panelRepresentante.paneldellogo.getHeight();

    //coordenadas y tamaño del recorte
    private double clipXx = 0;
    private double clipYy = 0;
    private static final double clipAnchos = 230;
    private static final double clipAltos = 210;

    private int Pos_Marca_Xx = 0;
    private int Pos_Marca_Yy = 0;

    private Color color_lineas = null;
    private final static float grosor_linea = 0.9f;
    private final static float dash1a[] = {10.0f};
    private final static BasicStroke dasheds
            = new BasicStroke(.5f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1a, 0.0f);
    private Paint painta;
    @Serial
    private static final long serialVersionUID = -4239501195415884385L;
}