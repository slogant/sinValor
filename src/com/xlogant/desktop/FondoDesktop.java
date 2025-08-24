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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 * Una implementación de {@link Border} que pinta una imagen de fondo,
 * estirándola para que se ajuste al tamaño del componente.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
public final class FondoDesktop implements Border {

    private static final Logger LOGGER = Logger.getLogger(FondoDesktop.class.getName());
    private final BufferedImage imagen;

    public FondoDesktop() {
        this("/com/xlogant/image/11.jpg");
    }

    /**
     * Crea un borde con una imagen de fondo desde la ruta especificada en el classpath.
     *
     * @param imagePath la ruta a la imagen dentro del classpath.
     * @throws RuntimeException si la imagen no se puede cargar.
     */
    public FondoDesktop(String imagePath) {
        try {
            this.imagen = ImageIO.read(Objects.requireNonNull(
                    getClass().getResource(imagePath), "No se pudo encontrar el recurso de imagen: " + imagePath
            ));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Fallo al cargar la imagen de fondo desde: " + imagePath, ex);
            // Lanzar una excepción en tiempo de ejecución es apropiado aquí,
            // ya que el componente no puede funcionar sin su imagen.
            throw new RuntimeException("No se pudo cargar la imagen de fondo", ex);
        }
    }

    /**
     * Pinta la imagen de fondo, escalada para llenar el área del componente.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // Dibuja la imagen usando el ancho y alto del componente, no de la pantalla.
        // Los parámetros x e y ya definen la posición del borde.
        g.drawImage(imagen, x, y, width, height, null);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        // Es más seguro devolver false a menos que se sepa que la imagen no tiene transparencia.
        return false;
    }
}
