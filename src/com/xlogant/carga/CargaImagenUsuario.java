/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.carga;

import com.xlogant.conecta.ConectaDB;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import static com.xlogant.conecta.ConectaDB.obtenerConexion;
import static java.lang.System.getProperty;
import static java.time.LocalDateTime.*;

/**
 *
 * @author oscar
 */
final public class CargaImagenUsuario implements Serializable {

    static public ImageIcon cargaImagenDB(String id, String nombre) {
        var ID = Integer.valueOf(id);
        System.out.println(ID + " " + nombre);
        var conecta = obtenerConexion();
        try (conecta;
                var pstmns = conecta.prepareStatement(TRAEIMAGEN);) {
            pstmns.setInt(1, ID);
            pstmns.setString(2, nombre);
            try (var resultados = pstmns.executeQuery();) {
                while (resultados.next()) {
                    /*el_blob = resultados.getBlob(1);
                 mer = el_blob.getBinaryStream();
                 guardaImagen(mer);*/
                    inpuImagen = resultados.getBinaryStream(1);
                    bufferImagen = ImageIO.read(inpuImagen);
                    icono = new ImageIcon(bufferImagen);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CargaImagenUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("El error: +++++++++++++++++++" + ex.getLocalizedMessage());
            System.out.println("Error de image:  " + ex.getLocalizedMessage());
        }
        return icono;
    }

    static public BufferedImage cargaLaImagenDB(String id, String nombre) {
        var ID = Integer.valueOf(id);
        //System.out.println(ID + " " + nombres);
        var conecta = obtenerConexion();
        try (conecta;
                var pstmns = conecta.prepareStatement(TRAEIMAGEN);) {
            pstmns.setInt(1, ID);
            pstmns.setString(2, nombre);
            try (var resultados = pstmns.executeQuery();) {
                while (resultados.next()) {
                    inpuImagen = resultados.getBinaryStream(1);
                    bufferImagen = ImageIO.read(inpuImagen);
                    guardaImagen(id, nombre, bufferImagen);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CargaImagenUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("El error: +++++++++++++++++++" + ex.getLocalizedMessage());
        } 
        return bufferImagen;
    }
    /*
    Falta cambiar el error de windows para guardar la  imagen
    */
    static private void guardaImagen(String u, String noms, BufferedImage buff) {
        try {
            var nombres = getArchivo(u, noms);
            byte[] imageInByte;
            try (var baos = new ByteArrayOutputStream()) {
                ImageIO.write(buff, "jpeg", baos);
                baos.flush();
                imageInByte = baos.toByteArray();
            }
            String direccion = getProperty("user.dir");
            var ins = new ByteArrayInputStream(imageInByte);
            var bImageFromConvert = ImageIO.read(ins);
            //ImageIO.write(bImageFromConvert, "jpg", new File("/home/oscar/NetBeansProjects/Imagenes/Todo/nessita.jpg"));
            var sistema = getProperty("os.name");
            var carpeta = new File(direccion + "\\test\\ ");
            var carpetaLinux = new File(direccion + "/.tests/");
            System.out.println(sistema);
            if (sistema.startsWith("Windows")) {
                if (!carpeta.exists()) {
                    carpeta.mkdir();
                    System.out.println("Creando carpeta------------->>>>>>>>>>>>>" + carpeta);
                    //correjir el error de guardar imagen
                    //ImageIO.write(bImageFromConvert, "jpg", new File(carpeta + nombres + ".jpeg"));
                } else if(carpeta.exists()) {
                    System.out.println(direccion);
                    System.out.println("La carpeta ya esta creada" + carpeta);
                    //correjir el error de guardar imagen
                    //ImageIO.write(bImageFromConvert, "jpg", new File(carpeta + nombres + ".jpeg"));
                    System.out.println("Se guardo imagen......................");

                    //ImageIO.write(bImageFromConvert, "jpg", new File(carpeta + nombres + ".jpeg"));
                }
                // includes: Windows 2000,  Windows 95, Windows 98, Windows NT, Windows Vista, Windows XP
                //ImageIO.write(bImageFromConvert, "jpg", new File(direccion + "\\tests\\" + nombres + ".jpeg"));
                System.out.println("Se Ejecuto");
            } else if (sistema.startsWith("Linux")) {
                System.out.println("No es windows");
                if (!carpetaLinux.exists()) {
                    carpetaLinux.mkdir();
                    System.out.println("Creando carpeta en linux");
                    ImageIO.write(bImageFromConvert, "jpg", new File(carpetaLinux + nombres + ".jpeg"));
                } else {
                    System.out.println("La carpeta ya esta creada en linux");
                    var fc = new File(carpetaLinux + nombres + ".jpeg");
                    if (fc.exists()) {
                        System.out.println("Se creo sin problemas");
                    } else {
                        System.out.println("No se puede crear");
                    }
                    //ImageIO.write(bImageFromConvert, "jpg", new File(carpetaLinux + nombres + ".jpeg"));
                }
                // everything else
                //ImageIO.write(bImageFromConvert, "jpg", new File(direccion + "/.tests/" + nombres + ".jpeg"));

            } else {
                System.out.println("Mac :-(");
            }
        } catch (IOException e) {
            Logger.getLogger(CargaImagenUsuario.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error:   " + e.getLocalizedMessage());
            System.out.println(e.getCause());
        }
    }

    private static String getArchivo(String h, String nomss) {
        var lo = LocalDate.now();
        var fechita = now();
        var ui = 1_000_000;
        var iu = 100;
        var localesd = (int) (Math.random() * (ui - iu) + iu);
        return h + " _" + nomss + " -> " + String.valueOf(localesd)
                + "__" + lo + " _" + fechita.getHour() + ":" + fechita.getMinute()
                + ":" + fechita.getSecond() + "." + fechita.getNano();
    }

    @Serial
    private static final long serialVersionUID = -1257200097624081967L;
    private static BufferedImage bufferImagen;
    private static InputStream inpuImagen;
    private static ImageIcon icono;
    private final static String TRAEIMAGEN = "SELECT foto_usuario FROM datos_usuarios du "
            + "INNER JOIN datos_usuario dus "
            + "ON du.id_ctrol_usuario = dus.id_dato_control "
            + "WHERE du.id_ctrol_usuario = ? "
            + "AND dus.usuario_nombre= ?";
    /*private static Blob el_blob;
    private static InputStream mer;
    private static InputStream self;*/

}
