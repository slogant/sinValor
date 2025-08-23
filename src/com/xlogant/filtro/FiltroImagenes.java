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

import static java.lang.String.format;

/**
 *
 * @author oscar
 */
final public class FiltroImagenes extends FileFilter implements Serializable {

    public FiltroImagenes(String unaExtension, String unaDescripcion) {
        extension = unaExtension;
        descripcion = unaDescripcion;
    }

    @Override public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().endsWith(extension);
    }

    @Override public String getDescription() {
        return descripcion + format(" (*%s)", extension);
    }
    private String extension = null;
    private String descripcion = null;
    @Serial
    private static final long serialVersionUID = 5148735387016775865L;
}

