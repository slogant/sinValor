/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.filtro;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import javax.swing.filechooser.FileFilter;

/**
 * Un {@link FileFilter} para {@link javax.swing.JFileChooser} que filtra
 * archivos basándose en una extensión específica, de forma insensible a mayúsculas/minúsculas.
 *
 * @author oscar
 * @author oscar (refactored by Gemini)
 */
final public class FiltroImagenes extends FileFilter implements Serializable {

    @Serial
    private static final long serialVersionUID = 5148735387016775865L;

    private final String extension;
    private final String descripcion;

    /**
     * Crea un nuevo filtro de imágenes.
     *
     * @param unaExtension   la extensión de archivo a aceptar (ej. "jpg", ".png").
     * @param unaDescripcion la descripción que se mostrará en el diálogo de selección.
     * @throws IllegalArgumentException si la extensión o la descripción son nulas o vacías.
     */
    public FiltroImagenes(String unaExtension, String unaDescripcion) {
        if (unaExtension == null || unaExtension.isBlank()) {
            throw new IllegalArgumentException("La extensión no puede ser nula o vacía.");
        }
        if (unaDescripcion == null || unaDescripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía.");
        }

        // Normalizar la extensión: asegurar que empiece con '.' y esté en minúsculas.
        this.extension = unaExtension.startsWith(".") ? unaExtension.toLowerCase() : "." + unaExtension.toLowerCase();
        this.descripcion = unaDescripcion;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        // Comparación insensible a mayúsculas/minúsculas.
        return f.getName().toLowerCase().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return String.format("%s (*%s)", descripcion, extension);
    }
}
