/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.acciones;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Un JPanel especializado en mostrar una BufferedImage.
 * Proporciona métodos para guardar la imagen en el sistema de archivos.
 *
 * @author oscar
 */
public final class ImagePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -4102449963762958616L;

    /**
     * Formateador para crear nombres de archivo seguros basados en la fecha y hora.
     */
    private static final DateTimeFormatter FILE_SAFE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private BufferedImage image;

    public ImagePanel() {
        super();
        // Es una buena práctica establecer un color de fondo por si no hay imagen.
        this.setBackground(Color.DARK_GRAY);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        // Vuelve a pintar el panel para mostrar la nueva imagen.
        this.repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Guarda la imagen actual en un archivo en la ruta especificada.
     * Si el directorio padre no existe, intentará crearlo.
     *
     * @param targetFile La ruta completa del archivo donde se guardará la imagen (ej. Path.of("C:/imagenes/foto.png")).
     * @throws IOException si la imagen es nula, si ocurre un error al crear directorios o al escribir el archivo.
     */
    public void saveImageToFile(Path targetFile) throws IOException {
        if (image == null) {
            throw new IOException("No hay imagen para guardar.");
        }

        // Asegurarse de que el directorio padre exista
        Path parentDir = targetFile.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        // Extraer la extensión del archivo para determinar el formato.
        String fileName = targetFile.getFileName().toString();
        String extension = "png"; // Formato por defecto
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1);
        }

        // Escribir la imagen en el archivo. ImageIO maneja los formatos comunes.
        boolean success = ImageIO.write(image, extension, targetFile.toFile());
        if (!success) {
            throw new IOException("No se encontró un escritor de imágenes adecuado para el formato: " + extension);
        }
    }

    /**
     * Guarda la imagen en un directorio por defecto dentro de la carpeta de Documentos del usuario.
     * El nombre del archivo se genera automáticamente con la fecha y hora actuales.
     *
     * @throws IOException si ocurre un error al guardar el archivo.
     */
    public void saveImageToDefaultDirectory() throws IOException {
        Path documentsDir = Paths.get(System.getProperty("user.home"), "Documentos");
        Path targetDir = documentsDir.resolve("MisImagenesApp"); // Subdirectorio para la app
        String timestamp = LocalDateTime.now().format(FILE_SAFE_FORMATTER);
        String fileName = "captura-" + timestamp + ".png";
        Path targetFile = targetDir.resolve(fileName);

        saveImageToFile(targetFile);
        System.out.println("Imagen guardada en: " + targetFile);
    }


    /**
     * Sobrescribe el método de pintado para dibujar la imagen.
     * Se usa paintComponent en lugar de paint para un comportamiento correcto en Swing.
     *
     * @param g el contexto gráfico.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paint(g);
        if (image != null) {
            // Dibuja la imagen escalada para que ocupe todo el tamaño del panel.
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
