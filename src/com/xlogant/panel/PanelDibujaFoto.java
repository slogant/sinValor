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
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import static java.lang.System.err;
import javax.swing.JPanel;

/**
 *
 * @author oscar
 */
public class PanelDibujaFoto extends JPanel implements Serializable {

    public PanelDibujaFoto(BufferedImage unBuffereds) {
        bufferss = unBuffereds;
        conImagens = true;
        this.setSize(new Dimension(anchurax, alturax));
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        Graphics2D g2dxx = (Graphics2D) g;
        //g2.setPaint(paint);
        if (conImagens) {
            otrass = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2Dss = otrass.createGraphics();
            g2Dss.setComposite(AlphaComposite.Src);
            g2Dss.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2Dss.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2Dss.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2Dss.drawImage(bufferss, 0, 0, anchurax, alturax, null);
            if (otrass != null) {
                g2dxx.drawImage(otrass, 0, 0, anchurax, alturax, null);
                g2dxx.dispose();
                repaint();
            } else {
                err.println("Vacio");
            }
        }
    }

    /**
     *
     * @return
     */
    public BufferedImage devuelveImagenes() {
        double clipYy = 0;
        //coordenadas y tamaño del recorte
        double clipXx = 0;
        BufferedImage imagenRecortess = ((BufferedImage) otrass).getSubimage((int) clipXx, (int) clipYy, (int) clipAnchos, (int) clipAltos);
        return imagenRecortess;
    }

    @Serial
    private static final long serialVersionUID = 5402439651289143416L;
    private BufferedImage bufferss = null;
    private BufferedImage otrass = null;
    private boolean conImagens = false;
    private final int anchurax = 215;
    private final int alturax = 252;

    private static final double clipAnchos = 230;
    private static final double clipAltos = 210;

    //variables para ek movimiento
    private int Pos_Marca_new_Xx = 0;
    private int Pos_Marca_new_Yy = 0;
    private int Pos_Marca_Xx = 0;
    private int Pos_Marca_Yy = 0;
    private int Dist_Xx = 0;
    private int Dist_Yy = 0;

    private Color color_lineass = null;
    private final static float grosor_lineas = 0.9f;
    private final static float dash1a[] = {10.0f};
    private final static BasicStroke dasheds
            = new BasicStroke(.5f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1a, 0.0f);
    private Paint painta;

}
