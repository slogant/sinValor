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
import java.awt.RenderingHints;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Un JPanel que pinta un fondo con un gradiente de color personalizable.
 * Permite configurar dos colores y la dirección del gradiente (vertical,
 * horizontal o diagonal).
 *
 * @author Oscar
 * @author Oscar (refactored by Gemini)
 */
final public class JPanelGradient extends JPanel {

    @Serial
    private static final long serialVersionUID = 8566914509152463093L;

    private Color color1 = new Color(0, 0, 25);
    private Color color2 = new Color(245, 245, 245);
    private GradientDirection direction = GradientDirection.VERTICAL;

    /**
     * Crea un panel con un gradiente por defecto (vertical, de azul oscuro a blanco).
     */
    public JPanelGradient() { }

    /**
     * Crea un panel con un gradiente personalizado.
     *
     * @param color1    el primer color del gradiente.
     * @param color2    el segundo color del gradiente.
     * @param direction la dirección del gradiente.
     */
    public JPanelGradient(Color color1, Color color2, GradientDirection direction) {
        this.color1 = color1;
        this.color2 = color2;
        this.direction = direction;
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Es crucial llamar al método de la superclase primero.
        Graphics2D g2d = (Graphics2D) g;

        // Activa el anti-aliasing para un gradiente de mayor calidad.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = switch (direction) {
            case HORIZONTAL -> new GradientPaint(0, height / 2f, color1, width, height / 2f, color2);
            case DIAGONAL_DOWN -> new GradientPaint(0, 0, color1, width, height, color2);
            case DIAGONAL_UP -> new GradientPaint(0, height, color1, width, 0, color2);
            default -> // VERTICAL
                    new GradientPaint(0, 0, color1, 0, height, color2);
        };

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(final Color color1) {
        if (color1 == null) {
            throw new IllegalArgumentException("El color 1 no puede ser nulo.");
        }
        this.color1 = color1;
        repaint(); // Vuelve a pintar el componente para reflejar el cambio.
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(final Color color2) {
        if (color2 == null) {
            throw new IllegalArgumentException("El color 2 no puede ser nulo.");
        }
        this.color2 = color2;
        repaint(); // Vuelve a pintar el componente para reflejar el cambio.
    }

    public GradientDirection getDirection() {
        return direction;
    }

    public void setDirection(final GradientDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException("La dirección no puede ser nula.");
        }
        this.direction = direction;
        repaint(); // Vuelve a pintar el componente para reflejar el cambio.
    }
}
