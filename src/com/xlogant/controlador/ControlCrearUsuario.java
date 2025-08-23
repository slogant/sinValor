/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.controlador;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.xlogant.principal.CentroPrincipal;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static com.xlogant.conecta.ConectaDB.*;
import static com.xlogant.encripta.EncriptaGenerador.getHash;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showInternalMessageDialog;

/**
 * @author oscar
 */
final public class ControlCrearUsuario implements Serializable {

    static public Map<String, Integer> obtenerRoles() {
        Map<String, Integer> mapaEstados = new TreeMap<>();
        var conectando = obtenerConexion();
        try (conectando) {
            try (var preparesta = conectando.prepareStatement(CONSULTAROLES)) {
                try (var resultados = preparesta.executeQuery()) {
                    while (resultados.next()) {
                        var ides = resultados.getInt(1);
                        var nombreRoles = resultados.getString(2);
                        mapaEstados.put(nombreRoles, ides);
                        
                    }
                    if (mapaEstados.isEmpty()) {
                        System.out.println("No se cargaron los registros");
                        return null;
                    } else {
                        return mapaEstados;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    public static Map<String, Integer> listaMenu() {
        Map<String, Integer> listadeMenu = new TreeMap<>();
        var conec = obtenerConexion();
        try (conec) {
            try (var ppst = conec.prepareStatement(CONSULTAMENU)) {
                try (var res = ppst.executeQuery();) {
                    return getStringIntegerMap(res, listadeMenu);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    private static Map<String, Integer> getStringIntegerMap(ResultSet res, Map<String, Integer> listadeMenu) throws SQLException {
        while (res.next()) {
            var IDMENU = res.getInt(1);
            var nomMenu = res.getString(2);
            listadeMenu.put(nomMenu, IDMENU);
        }
        if (listadeMenu.isEmpty()) {
            System.out.println("No se cargaron los registros");
            return null;
        } else {
            return listadeMenu;
        }
    }

    public static Map<String, Integer> listaSubmenu() {
        Map<String, Integer> listaSubmenus = new TreeMap<>();
        var con = obtenerConexion();
        try (var prepare = con.prepareStatement(CONSULTASUBMENU);
             var rest = prepare.executeQuery();) {
            return getStringIntegerMap(rest, listaSubmenus);
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    public static Map<String, Long> listaEmpresa() {
        ListaLasEmpresas = new TreeMap<>();
        var conecta = obtenerConexion();
        try (conecta;
             var ps = conecta.prepareStatement(CONSULTAEMPRESA);
             var resulta = ps.executeQuery();) {
            while (resulta.next()) {
                var id_emp = resulta.getLong(1);
                var razon = resulta.getString(2);
                ListaLasEmpresas.put(razon, id_emp);
            }
            if (ListaLasEmpresas.isEmpty()) {
                System.out.println("No se cargaron los registros de la empresa");
                return null;
            } else {
                return ListaLasEmpresas;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    public static boolean controlaNombre(String nombres) {
        var encontrar = false;
        if (nombres.isBlank()) {
            System.out.println("El campo nombre esta vacío");
            return false;
        } else {
            var cnt = obtenerConexion();
            try (cnt;
                 var pt = cnt.prepareStatement(CONSULTANOMBRE);) {
                pt.setString(1, nombres);
                try (var rt = pt.executeQuery();) {
                    while (rt.next()) {
                        elNombreUsuario = rt.getString(1);
                        encontrar = rt.wasNull();
                    }
                    System.out.printf("El resultado esta vacio: %b%n", encontrar);
                    return (nombres.equals(elNombreUsuario));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error: " + ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
                return false;
            }
        }
    }

    public static boolean controlaNumeroEmp(String unNumEmp) {
        if (unNumEmp.isBlank()) {
            System.out.println("El campo número de empleado esta vacío");
            return false;
        } else {
            var cont = obtenerConexion();
            try (cont;
                 var outprep = cont.prepareStatement(CONSULTANUMEMPLEADO);) {
                outprep.setString(1, unNumEmp);
                try (var requerido = outprep.executeQuery();) {
                    while (requerido.next()) {
                        elNumeroEmp = requerido.getString(1);
                    }
                    return unNumEmp.equals(elNumeroEmp);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControlCrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error: " + ex.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
                return false;
            }
        }
    }

    public static void guardaEnBase(String nombrePersona, String apellidoPatPer, String apellidoMatPer, String direccionPersona,
                                    String telefPersona, String celPersona, String mailPersona, LocalDateTime timedate, String numEmp, long IDMPRESA,
                                    BufferedImage fotografia, String NombreUsuario, String clave, boolean status, int idrole,
                                    long sucursalID, EnumMap<DPFPFingerIndex, DPFPTemplate> mpHuellas, String rfid) {
        final int controlaId;
        final boolean inserta, inserta1, inserta3;
        System.out.println("El rol: -------------------============================================>>>>>>>>>>>   " + idrole);

        boolean checa;
        switch (idrole) {
            case 2 -> {
                System.out.println("el roleeeeeeeeeeeeeeeeee: " + idrole);
                inserta = insertaDatosUsuario(nombrePersona, apellidoPatPer, apellidoMatPer, direccionPersona,
                        telefPersona, celPersona, mailPersona, fotografia, numEmp, IDMPRESA,
                        NombreUsuario, clave, status, idrole, sucursalID, rfid);
                if (inserta) {
                    System.out.println("Datos usuario ingresados correctamente---------------> " + inserta);
                    inserta1 = insertaUsuario(NombreUsuario, clave, status, idrole);
                    if (inserta1) {
                        System.out.println("Campos especificos del usuario guardado " + inserta1);
                        controlaId = devuelveID(numEmp);
                        if (controlaId != 0) {
                            System.out.println("Obteniedo el id: " + controlaId);
                            System.out.println("El Role: " + idrole);
                            /////////////////////////////////////////////////////////
                            checa = insertaHuellasDB(controlaId, mpHuellas);
                            System.out.println("Guardando huellas: " + checa);
                            /////////////////////////////////////////////////////////
                            if (checa) {
                                inserta3 = integraUsuario(controlaId, idrole);
                                if (inserta3) {
                                    System.out.println("Permisos agregados");
                                    devuelveSubmenusEnBase(idrole, controlaId);
                                    /*if (inserta4) {
                                    System.out.println("Permisos submenus agredados");
                                    } else {
                                    System.out.println("No se agregaron los submenus");
                                    }*/
                                } else {
                                    System.out.println("No se agregaron los permisos");
                                }
                            } else {
                                System.out.println("No se guardaron la huellas");
                            }
                        } else {
                            System.out.println("Error al devolver el id del usuario");
                        }
                    } else {
                        System.out.println("No se efectuo la operacion 2");
                    }
                } else {
                    System.out.println("No se efectuo la operacion 1");
                }
            }
            case 3, 4, 5, 6, 7, 8, 9, 10 -> {
                System.out.println("el roleeeeeeeeeeeeeeeeee: " + idrole);
                inserta = insertaDatosUsuario(nombrePersona, apellidoPatPer, apellidoMatPer, direccionPersona,
                        telefPersona, celPersona, mailPersona, fotografia, numEmp, IDMPRESA,
                        NombreUsuario, clave, status, idrole, sucursalID, rfid);
                if (inserta) {
                    System.out.println("Datos usuario ingresados correctamente " + inserta);
                    inserta1 = insertaUsuario(NombreUsuario, clave, status, idrole);
                    if (inserta1) {
                        System.out.println("Campos especificos del usuario guardado " + inserta1);
                        controlaId = devuelveID(numEmp);
                        if (controlaId != 0) {
                            System.out.println("Obteniedo el id: " + controlaId);
                            System.out.println("El Role: " + idrole);
                            checa = insertaHuellasDB(controlaId, mpHuellas);
                            if (checa) {
                                inserta3 = integraUsuario(controlaId, idrole);
                                if (inserta3) {
                                    System.out.println("Permisos agregados");
                                    devuelveSubmenusEnBase(idrole, controlaId);
                                    /*if (inserta4) {
                                    System.out.println("Permisos submenus agredados");
                                    } else {
                                    System.out.println("No se agregaron los submenus");
                                    }*/
                                } else {
                                    System.out.println("No se agregaron los permisos");
                                }
                            } else {
                                System.out.println("No se guardaron la huellas");
                            }
                        } else {
                            System.out.println("Error al devolver el id del usuario");
                        }
                    } else {
                        System.out.println("No se efectuo la operacion 2");
                    }
                } else {
                    System.out.println("No se efectuo la operacion 1");
                }
            }
            default -> System.out.println("Usuario terminado....................");
        }
    }

    private static boolean insertaDatosUsuario(String nom, String apellidoPat,
                                               String apellidoMat, String direc, String telef, String cel,
                                               String mail, BufferedImage imagen, String numEmpleado,
                                               long IDEMP, String nomus, String pass, boolean sta, long rol, long sucID, String RFID) {
        LocalDateTime fecha = LocalDateTime.now();
        try {
            ByteArrayOutputStream byatos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "jpg", byatos);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byatos.toByteArray());
            Timestamp t = Timestamp.valueOf(fecha);
            try (var conectando = obtenerConexion()) {
                try (conectando; var prepare = conectando.prepareStatement(DATOSUSUARIO);) {
                    prepare.setString(1, nom);
                    prepare.setString(2, apellidoPat);
                    prepare.setString(3, apellidoMat);
                    prepare.setString(4, direc);
                    prepare.setString(5, telef);
                    prepare.setString(6, cel);
                    prepare.setString(7, mail);
                    prepare.setBinaryStream(8, inputStream, inputStream.available());
                    prepare.setTimestamp(9, t);
                    prepare.setString(10, numEmpleado);
                    prepare.setLong(11, IDEMP);
                    prepare.setLong(12, sucID);
                    prepare.setString(13, RFID);
                    int integra = prepare.executeUpdate();
                    spt1 = conectando.setSavepoint("Punto1");
                    cerrarCommit(conectando);
                    return (integra == 1);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            System.exit(1);
            try {
                conectado.rollback(spt1);
                rollback(conectado);
            } catch (SQLException ex1) {
                Logger.getLogger(ControlCrearUsuario.class
                        .getName()).log(Level.SEVERE, null, ex1);
                System.out.println("Error: " + ex1.getLocalizedMessage());
                showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            }
            return false;
        }
    }

    private static boolean insertaUsuario(String nomus, String pass,
                                          boolean sta, long rol) {
        var encriptado = getHash(pass, 5);
        var cc = obtenerConexion();
        try (cc; var pp = cc.prepareStatement(USUARIOLOCAL);) {
            pp.setString(1, nomus);
            pp.setString(2, encriptado);
            pp.setBoolean(3, sta);
            pp.setLong(4, rol);
            var integrado = pp.executeUpdate();
            cerrarCommit(cc);
            return (integrado == 1);
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            rollback(cc);
            return false;
        }
    }

    private static boolean insertaHuellasDB(long UnidUs, EnumMap<DPFPFingerIndex, DPFPTemplate> unasHuellas) {
        muestraID = UnidUs;
        var ldt = LocalDateTime.now();
        var ttr = Timestamp.valueOf(ldt);
        var cnx = obtenerConexion();
        try (cnx;
             var pre = cnx.prepareStatement(INSERTAHUELLA);) {
            unasHuellas.forEach((nom, h) -> {
                try {
                    var rrs = (String) nom.toString();
                    var datosHuella = new ByteArrayInputStream(h.serialize());
                    var sizeHuella = h.serialize().length;
                    pre.setLong(1, muestraID);
                    pre.setString(2, rrs);
                    pre.setBinaryStream(3, datosHuella, sizeHuella);
                    pre.setTimestamp(4, ttr);
                    pre.addBatch();
                } catch (SQLException ex) {
                    Logger.getLogger(ControlCrearUsuario.class
                            .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error al guardar las huellas: " + ex.getLocalizedMessage());
                    System.out.println("Error: " + ex.getSQLState());
                    showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
                }
            });
            var logro = pre.executeBatch();
            checaRegistro(logro);
            var listados = List.of(logro);
            listados.forEach(System.out::println);
            cerrarCommit(cnx);
            return checaVerdad(logro);
        } catch (SQLException e) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al guardar las huellas: " + e.getLocalizedMessage());
            System.out.println("Error: " + e.getSQLState());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            rollback(cnx);
            return false;
        }
    }

    private static void checaRegistro(int[] cuenta) {
        for (var id : cuenta) {
            if (cuenta[id] >= 0) {
                // Successfully executed; the number represents number of affected rows
                System.out.println("OK: Total de insert=" + cuenta[id]);
            } else if (cuenta[id] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                System.out.println("OK: Todos los registros ingresado");
            } else if (cuenta[id] == Statement.EXECUTE_FAILED) {
                System.out.println("No se pudierom ingresar los datos a la base");
            }
        }
        /*for (var i = 0; i < cuenta.length; i++) {
            if (cuenta[i] >= 0) {
                // Successfully executed; the number represents number of affected rows
                System.out.println("OK: Total de insert=" + cuenta[i]);
            } else if (cuenta[i] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                System.out.println("OK: Todos los registros ingresado");
            } else if (cuenta[i] == Statement.EXECUTE_FAILED) {
                System.out.println("No se pudierom ingresar los datos a la base");
            }
        }*/
    }

    private static boolean checaVerdad(int[] cuenta) {
        for (var id : cuenta) {
            return cuenta[id] > 0;
        }
        /*for (var i = 0; i < cuenta.length; i++) {
            return cuenta[i] > 0;
        }*/
        return false;
    }

    private static int devuelveID(String numemp) {
        var cdo = obtenerConexion();
        try (cdo;
             var pst = cdo.prepareStatement(CONSULTAIDEMPLEADO);) {
            pst.setString(1, numemp);
            ResultSet rset = pst.executeQuery();
            if (rset.next()) {
                devuelto = rset.getInt(1);
            }
            return devuelto;
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return 0;
        }
    }

    private static boolean integraUsuario(int idUs, int elRol) {
        unID = idUs;
        unRol = elRol;
        var cnn = obtenerConexion();
        try (cnn; var prep = cnn.prepareStatement(INSERTACONTROLMENU);) {
            var mapas = ControlMenu(unRol);
            System.out.println("Total de menus " + mapas.size());
            mapas.forEach((a, b) -> {
                try {
                    prep.setInt(1, unID);
                    prep.setInt(2, unRol);
                    prep.setInt(3, a);
                    prep.setBoolean(4, b);
                    prep.addBatch();
                } catch (SQLException ex) {
                    Logger.getLogger(ControlCrearUsuario.class
                            .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error: " + ex.getLocalizedMessage());
                    showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
                }
            });
            var logro = prep.executeBatch();
            checaRegistros(logro);
            cerrarCommit(cnn);
            var lo = List.of(logro);
            lo
                    .forEach(System.out::println);
            return (logro.length != 0);
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Un error: " + ex.getSQLState());
            System.out.println("El Error es: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            rollback(cnn);
            return false;
        }
    }

    private static void devuelveSubmenusEnBase(int captura, int idusua) {
        int valorA;
        int valorB;
        int valorCC;
        boolean valorDD;
        int valorR;
        int o = 0, i = 0;
        List<Integer> muestrameSub = new LinkedList<>();
        var loconecte = obtenerConexion();
        try (loconecte;
             var prepara = loconecte.prepareStatement(ConsultaSubmenusDEBASE);
             var reprepare = loconecte.prepareStatement(INGRESAELSUBMENU);) {
            prepara.setInt(1, captura);
            ResultSet result = prepara.executeQuery();
            while (result.next()) {
                valorA = result.getInt(1);
                valorB = result.getInt(2);
                valorCC = result.getInt(3);
                valorDD = result.getBoolean(4);
                //System.out.println(valorA + " -------- " + valorB + " ------- " + valorCC + "------- " + valorDD + " ------- Contando: " + i++);
                reprepare.setInt(1, valorA);
                reprepare.setInt(2, valorB);
                reprepare.setInt(3, valorCC);
                reprepare.setBoolean(4, valorDD);
                reprepare.setInt(5, idusua);
                reprepare.addBatch();
            }
            var logross = reprepare.executeBatch();
            checaRegistros(logross);
            cerrarCommit(loconecte);
            var listado = List.of(logross);
            listado.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error al traer los submenus: " + e.getLocalizedMessage());
            System.out.println("Error: " + e.getSQLState());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, e.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            System.exit(0);
            //return false;
        }
    }

    private static Map<Integer, Boolean> ControlMenu(int roles) {
        System.out.println("El role es: " + roles);
        Map<Integer, Boolean> listaControl = new TreeMap<>();
        var abreConexion = obtenerConexion();
        try (abreConexion;
             var sinprep = abreConexion.prepareStatement(CONSULTAMEELMENU);) {
            sinprep.setInt(1, roles);
            try (var adquiere = sinprep.executeQuery();) {
                while (adquiere.next()) {
                    entero = adquiere.getInt(1);
                    muestra = adquiere.getString(2);
                    boolean estatusm = adquiere.getBoolean(3);
                    System.err.println("<<<-<----------------------------------------------->>>>>>>>>><");
                    System.out.println(muestra + " ------------- " + estatusm);
                    System.err.println("<<<<<<<<<<<------------------------------------------>>>>>>>>>>>>");
                    listaControl.put(entero, estatusm);
                }
                if (listaControl.isEmpty()) {
                    System.out.println("Lista vacia");
                    return null;
                } else {
                    return listaControl;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("El Error es aquí: " + ex.getLocalizedMessage());
            System.out.println("El Error es: " + ex.getSQLState());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    private static boolean integraSubmenus(int unId, int unRol) {
        IIDS = unId;
        URROLS = unRol;
        var estaconectado = obtenerConexion();
        try (estaconectado; var preparado = estaconectado.prepareStatement(INSERTACONTROLMENU);) {
            var mapas = ControlMenu(URROLS);
            System.out.println("Total de menus " + mapas.size());
            mapas.forEach((a, b) -> {
                try {
                    //lalista.add(a);
                    prep.setInt(1, IIDS);
                    prep.setInt(2, URROLS);
                    prep.setInt(3, a);
                    prep.setBoolean(4, b);
                    prep.addBatch();
                } catch (SQLException ex) {
                    Logger.getLogger(ControlCrearUsuario.class
                            .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Un error: " + ex.getSQLState());
                    System.out.println("El Error es: " + ex.getLocalizedMessage());
                    showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
                }

            });
            var logro = prep.executeBatch();
            checaRegistros(logro);
            var listados = List.of(logro);
            listados.forEach(System.out::println);
            return (logro.length != 0);
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Un error: " + ex.getSQLState());
            System.out.println("El Error es: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return false;
        }
    }

    private static Map<Integer, String> ContrlSubMenu(int idUs, int elRol) {
        Map<Integer, String> listaControlSub = new TreeMap<>();
        var establece = obtenerConexion();
        try (establece;
             var preparado = establece.prepareStatement(CONSUBMENUS);) {
            preparado.setInt(1, elRol);
            preparado.setBoolean(2, ACTIVOS);
            preparado.setInt(3, idUs);
            preparado.setBoolean(4, ACTIVOS);
            try (var resulto = preparado.executeQuery();) {
                while (resulto.next()) {
                    entero = resulto.getInt(2);
                    muestra = resulto.getString(3);
                    listaControlSub.put(entero, muestra);
                }
                if (listaControlSub.isEmpty()) {
                    System.out.println("Lista vacia");
                    return null;
                } else {
                    return listaControlSub;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("El Error es aquí: " + ex.getLocalizedMessage());
            System.out.println("El Error es: " + ex.getSQLState());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    public static void checaRegistros(int[] cuentas) {
        for (var id : cuentas) {
            if (cuentas[id] >= 0) {
                // Successfully executed; the number represents number of affected rows
                System.out.println("OK: Total de insert=" + cuentas[id]);
            } else if (cuentas[id] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                System.out.println("OK: Todos los registros ingresado");
            } else if (cuentas[id] == Statement.EXECUTE_FAILED) {
                System.out.println("No se pudierom ingresar los datos a la base");
            }
        }
        /*for (var i = 0; i < cuentas.length; i++) {
            if (cuentas[i] >= 0) {
                // Successfully executed; the number represents number of affected rows
                System.out.println("OK: Total de insert=" + cuentas[i]);
            } else if (cuentas[i] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                System.out.println("OK: Todos los registros ingresado");
            } else if (cuentas[i] == Statement.EXECUTE_FAILED) {
                System.out.println("No se pudierom ingresar los datos a la base");
            }
        }*/
    }

    public static Map<String, Long> listaEmpresaActiva(String nom) {
        ListaLasEmpresas = new TreeMap<>();
        var conecta = obtenerConexion();
        try (conecta; var ps = conecta.prepareStatement(CONSULTANDOEMPRESA);) {
            ps.setString(1, nom);
            ps.setBoolean(2, ACTIVOS);
            ps.setBoolean(3, ACTIVOS);
            ps.setBoolean(4, ACTIVOS);
            try (var resulta = ps.executeQuery();) {
                while (resulta.next()) {
                    var id_emp = resulta.getLong(1);
                    var razon = resulta.getString(2);
                    ListaLasEmpresas.put(razon, id_emp);
                }
                if (ListaLasEmpresas.isEmpty()) {
                    System.out.println("No se cargaron los registros de la empresa");
                    ListaLasEmpresas.put("Registro no encontrado", 0L);
                    return ListaLasEmpresas;
                } else {
                    return ListaLasEmpresas;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    public static Map<String, Long> listaSucursales(long idprem) {
        Map<String, Long> listaSuc = new TreeMap<>();
        var cone = obtenerConexion();
        try (cone;
             var ptx = cone.prepareStatement(CONSULTANDOSUCURSAL);) {
            ptx.setLong(1, idprem);
            ptx.setBoolean(2, ACTIVOS);
            ptx.setBoolean(3, ACTIVOS);
            ptx.setBoolean(4, ACTIVOS);
            try (var rx = ptx.executeQuery();) {
                while (rx.next()) {
                    long enIds = rx.getInt(4);
                    String laSucur = rx.getString(5);
                    listaSuc.put(laSucur, enIds);
                }
                if (listaSuc.isEmpty()) {
                    System.out.println("la lista sucursal esta vacia");
                    listaSuc.put("Sin registro de sucursal", 0L);
                } else {
                    System.out.println("Valores de la sucursal devueltos");
                }
                return listaSuc;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlCrearUsuario.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getLocalizedMessage());
            showInternalMessageDialog(CentroPrincipal.jDesktopPane1, ex.getLocalizedMessage(), "Monitor", INFORMATION_MESSAGE);
            return null;
        }
    }

    private static Connection conectado;
    private static PreparedStatement pstmn, prep;

    private final static String CONSULTAROLES = "SELECT cu.id_role,cr.nombre_role FROM control_roles cr "
            + "INNER JOIN control_usuario cu "
            + "ON cu.id_control_us = cr.id_role_control "
            + "WHERE cu.status_role = true "
            + "ORDER BY cu.id_role ASC "
            + "LIMIT 10 OFFSET 1";

    private final static String CONSULTAMENU = "SELECT id_menu, nombre_menu FROM menu_principal";

    private final static String CONSULTAMEELMENU = "SELECT mp.id_menu, mp.nombre_menu,vm.estatus_menu FROM vista_menu vm "
            + "INNER JOIN menu_principal mp "
            + "ON vm.id_ctrol_menu = mp.id_menu "
            + "WHERE vm.id_role= ?"
            + "ORDER BY vm.id_menu, vm.id_role,  vm.id_ctrol_menu ASC ";

    private final static String CONSULTASUBMENU = "SELECT id_submenu, nombre_submenu FROM submenu_principal";

    private final static String CONSULTAEMPRESA = "SELECT  ce.id_empresa, de.razon_social  FROM datos_empresa  de "
            + "INNER JOIN control_empresa ce "
            + "ON ce.id_empresa = de.id_empresa "
            + "WHERE ce.estatus_empresa = true";

    private final static String CONSULTANDOEMPRESA = "SELECT ce.id_empresa, de.razon_social, "
            + "ce.estatus_empresa, ds.id_sucursal, "
            + "ds.nombre_sucursal,csc.estatus_suc "
            + "FROM datos_empresa  de "
            + "INNER JOIN control_empresa ce "
            + "ON ce.id_empresa = de.id_empresa "
            + "INNER JOIN datos_sucursal ds "
            + "ON ds.id_empresa = de.id_empresa "
            + "INNER JOIN control_suc csc "
            + "ON csc.id_suc = ds.id_sucursal "
            + "INNER JOIN datos_usuarios dus "
            + "ON dus.id_empresa = ce.id_empresa "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "WHERE du.usuario_nombre= ? "
            + "AND du.status_de_usuario= ? "
            + "AND ce.estatus_empresa= ? "
            + "AND csc.estatus_suc= ?";

    private final static String CONSULTANDOSUCURSAL = "SELECT ce.id_empresa, de.razon_social, "
            + "ce.estatus_empresa, ds.id_sucursal, "
            + "ds.nombre_sucursal,csc.estatus_suc "
            + "FROM datos_empresa  de "
            + "INNER JOIN control_empresa ce "
            + "ON ce.id_empresa = de.id_empresa "
            + "INNER JOIN datos_sucursal ds "
            + "ON ds.id_empresa = de.id_empresa "
            + "INNER JOIN control_suc csc "
            + "ON csc.id_suc = ds.id_sucursal "
            + "INNER JOIN datos_usuarios dus "
            + "ON dus.id_empresa = ce.id_empresa "
            + "INNER JOIN datos_usuario du "
            + "ON du.id_dato_control = dus.id_ctrol_usuario "
            + "WHERE ce.id_empresa= ? "
            + "AND du.status_de_usuario= ? "
            + "AND ce.estatus_empresa= ? "
            + "AND csc.estatus_suc= ?";

    private final static String LASSUCURSALES = "SELECT ds.id_sucursal, ds.nombre_sucursal from datos_sucursal ds "
            + "INNER JOIN control_suc cs "
            + "ON cs.id_suc = ds.id_sucursal "
            + "WHERE cs.estatus_suc= ?";

    private final static String DATOSUSUARIO = "INSERT INTO datos_usuarios(nombre,apellido_pat, apellido_mat, "
            + "direccion_us, telefono, celular, email, "
            + "foto_usuario, fecha_ingreso, numero_emp,"
            + " id_empresa, id_sucursal, rfid) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String CONSULTANOMBRE = "SELECT usuario_nombre FROM datos_usuario WHERE usuario_nombre= ?";

    private final static String USUARIOLOCAL = "INSERT INTO datos_usuario(usuario_nombre, clave_usuario, status_de_usuario, id_role) "
            + "VALUES(?, ?, ?, ?)";

    private final static String CONSULTAIDEMPLEADO = "SELECT dus.id_ctrol_usuario FROM datos_usuarios dus WHERE dus.numero_emp= ?";

    private final static String INSERTACONTROLMENU = "INSERT INTO control_menu(id_usuario, id_role, id_menu, estatus_menu) "
            + "VALUES(?, ?, ?, ?)";

    private final static String CONSULTANUMEMPLEADO = "SELECT numero_emp FROM datos_usuarios WHERE numero_emp= ?";

    private final static String INSERTAHUELLA = "INSERT INTO control_huellas(id_usuario, identificador_huella, huella_reg, fecha_ins) "
            + "VALUES(?, ?, ?, ?)";

    private final static String CONSUBMENUS = "SELECT cm.id_menu, "
            + "cm.id_role, "
            + "mp.nombre_menu, "
            + "sm.id_submenu"
            + "sm.nombre_submenu, "
            + "cs.estatus_submenus, "
            + "sm.descripcion_submenu "
            + "FROM control_menu cm "
            + "INNER JOIN menu_principal mp "
            + "ON cm.id_menu = mp.id_menu "
            + "INNER JOIN control_submenu cs "
            + "ON cs.id_menu = mp.id_menu "
            + "INNER JOIN submenu_principal sm "
            + "ON sm.id_submenu = cs.id_submenu "
            + "WHERE cm.id_role = ? "
            + "AND cm.estatus_menu = ? "
            + "AND mp.id_menu= ? "
            + "AND cs.id_usuario = ? "
            + "AND cs.estatus_submenus = ? "
            + "ORDER BY sm.id_submenu ASC";

    private final static String ConsultaSubmenusDEBASE = "SELECT DISTINCT id_role, id_menu, id_submenu, "
            + "estatus_submenus FROM permisos_menu "
            + "WHERE id_role= ? "
            + "ORDER BY id_role,id_menu,id_submenu ASC";

    private final static String INGRESAELSUBMENU = "INSERT INTO control_submenu(id_role, id_menu, id_submenu, estatus_submenus, id_usuario) "
            + "VALUES(?, ?, ?, ?, ?)";

    private static Map<String, Long> ListaLasEmpresas;
    private static Map<Integer, Long> meteValores;
    private static int entero;
    private static int unID;
    private static int unRol;
    private static int IIDS;
    private static int URROLS;
    private static int devuelto;
    private static long muestraID;
    private static String elNombreUsuario;
    private static String muestra;
    private static String elNumeroEmp;
    private final static boolean ACTIVOS = true;
    private static boolean inserta4;
    private static Savepoint spt1;
    private static Savepoint spt2;
    @Serial
    private static final long serialVersionUID = 3434091817377493717L;
}