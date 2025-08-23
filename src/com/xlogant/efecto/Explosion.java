/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.efecto;

import java.awt.Dimension;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 *
 * @author oscar
 */
final public class Explosion implements Serializable {

    public Explosion(JPanel container, JPanel content) {
        scheduler = newSingleThreadScheduledExecutor();
        this.container = container;
        this.content = content;
        this.container.removeAll();
        d = new Dimension(0,0);
        this.content.setSize(d);//tamaño inicial
        //coordenadas iniciales
        int ancho_min = 10;
        px = this.container.getSize().width / 2 - ancho_min / 2;
        int alto_min = 10;
        py = this.container.getSize().height / 2 - alto_min / 2;
        content.setLocation(px, py);
        content.setVisible(true);
        //se agrega al contenedor el JPanel contenido
        this.container.add(content);
        count = 10;
    }

    /**
     * Metodo para ejecutar el efecto EXPLODE
     */
    public void play() {
        scheduler = newSingleThreadScheduledExecutor();
        //en milisegundos
        var velocidad = 80;
        scheduler.scheduleAtFixedRate(() -> {
            //nuevo tamaño
            d = new Dimension(container.getSize().width * count / 100, container.getSize().height * count / 100);
            count = count + 10;
            //se recalcula la posicion mientras el jpanel crece
            px = container.getSize().width / 2 - d.width / 2;
            py = container.getSize().height / 2 - d.height / 2;
            content.setLocation(px, py);
            if (count > 100) {
                close();
            }
            content.setSize(d);
            container.updateUI();
        }, 100, velocidad, TimeUnit.MILLISECONDS);
    }

    /**
     * Metodo para terminar el efecto explode
     */
    public void close() {
        scheduler.shutdownNow();
    }

    @Serial
    private static final long serialVersionUID = -5900904192293377835L;
    private ScheduledExecutorService scheduler;
    private Dimension d;
    private int count = 0;
    private final JPanel container;
    private final JPanel content;
    //coordenadas del JPanel contenido
    private int px = 0;
    private int py = 0;
}
