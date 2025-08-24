package com.xlogant.controlador;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.xlogant.principal.CentroPrincipal;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static com.xlogant.conecta.ConectaDB.*;
import static com.xlogant.encripta.EncriptaGenerador.getHash;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showInternalMessageDialog;

/**
 * Controlador para la creación y gestión de usuarios del sistema.
 * Maneja la creación completa de usuarios incluyendo datos personales,
 * autenticación, huellas dactilares y permisos.
 * 
 * @author oscar
 */
public final class ControlCrearUsuario implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ControlCrearUsuario.class.getName());
    private static final boolean ACTIVOS = true;
    
    @Serial
    private static final long serialVersionUID = 3434091817377493717L;
    
    // Constantes para queries SQL
    private static final String CONSULTA_ROLES = 
        "SELECT cu.id_role, cr.nombre_role FROM control_roles cr " +
        "INNER JOIN control_usuario cu ON cu.id_control_us = cr.id_role_control " +
        "WHERE cu.status_role = true ORDER BY cu.id_role ASC LIMIT 10 OFFSET 1";
        
    private static final String CONSULTA_MENU = 
        "SELECT id_menu, nombre_menu FROM menu_principal";
        
    private static final String CONSULTA_SUBMENU = 
        "SELECT id_submenu, nombre_submenu FROM submenu_principal";
        
    private static final String CONSULTA_EMPRESA = 
        "SELECT ce.id_empresa, de.razon_social FROM datos_empresa de " +
        "INNER JOIN control_empresa ce ON ce.id_empresa = de.id_empresa " +
        "WHERE ce.estatus_empresa = true";
        
    private static final String CONSULTA_NOMBRE_USUARIO = 
        "SELECT usuario_nombre FROM datos_usuario WHERE usuario_nombre = ?";
        
    private static final String CONSULTA_NUMERO_EMPLEADO = 
        "SELECT numero_emp FROM datos_usuarios WHERE numero_emp = ?";
        
    private static final String INSERTAR_DATOS_USUARIO = 
        "INSERT INTO datos_usuarios(nombre, apellido_pat, apellido_mat, direccion_us, " +
        "telefono, celular, email, foto_usuario, fecha_ingreso, numero_emp, " +
        "id_empresa, id_sucursal, rfid) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
    private static final String INSERTAR_USUARIO_LOCAL = 
        "INSERT INTO datos_usuario(usuario_nombre, clave_usuario, status_de_usuario, id_role) " +
        "VALUES(?, ?, ?, ?)";
        
    private static final String INSERTAR_HUELLA = 
        "INSERT INTO control_huellas(id_usuario, identificador_huella, huella_reg, fecha_ins) " +
        "VALUES(?, ?, ?, ?)";
        
    private static final String CONSULTA_ID_EMPLEADO = 
        "SELECT dus.id_ctrol_usuario FROM datos_usuarios dus WHERE dus.numero_emp = ?";
        
    private static final String INSERTAR_CONTROL_MENU = 
        "INSERT INTO control_menu(id_usuario, id_role, id_menu, estatus_menu) " +
        "VALUES(?, ?, ?, ?)";
        
    private static final String CONSULTA_MENU_POR_ROL = 
        "SELECT mp.id_menu, mp.nombre_menu, vm.estatus_menu FROM vista_menu vm " +
        "INNER JOIN menu_principal mp ON vm.id_ctrol_menu = mp.id_menu " +
        "WHERE vm.id_role = ? ORDER BY vm.id_menu, vm.id_role, vm.id_ctrol_menu ASC";
        
    private static final String CONSULTA_SUBMENUS_BASE = 
        "SELECT DISTINCT id_role, id_menu, id_submenu, estatus_submenus " +
        "FROM permisos_menu WHERE id_role = ? ORDER BY id_role, id_menu, id_submenu ASC";
        
    private static final String INSERTAR_SUBMENU = 
        "INSERT INTO control_submenu(id_role, id_menu, id_submenu, estatus_submenus, id_usuario) " +
        "VALUES(?, ?, ?, ?, ?)";
        
    private static final String CONSULTA_EMPRESA_ACTIVA = 
        "SELECT ce.id_empresa, de.razon_social, ce.estatus_empresa, ds.id_sucursal, " +
        "ds.nombre_sucursal, csc.estatus_suc FROM datos_empresa de " +
        "INNER JOIN control_empresa ce ON ce.id_empresa = de.id_empresa " +
        "INNER JOIN datos_sucursal ds ON ds.id_empresa = de.id_empresa " +
        "INNER JOIN control_suc csc ON csc.id_suc = ds.id_sucursal " +
        "INNER JOIN datos_usuarios dus ON dus.id_empresa = ce.id_empresa " +
        "INNER JOIN datos_usuario du ON du.id_dato_control = dus.id_ctrol_usuario " +
        "WHERE du.usuario_nombre = ? AND du.status_de_usuario = ? " +
        "AND ce.estatus_empresa = ? AND csc.estatus_suc = ?";
        
    private static final String CONSULTA_SUCURSALES = 
        "SELECT ce.id_empresa, de.razon_social, ce.estatus_empresa, ds.id_sucursal, " +
        "ds.nombre_sucursal, csc.estatus_suc FROM datos_empresa de " +
        "INNER JOIN control_empresa ce ON ce.id_empresa = de.id_empresa " +
        "INNER JOIN datos_sucursal ds ON ds.id_empresa = de.id_empresa " +
        "INNER JOIN control_suc csc ON csc.id_suc = ds.id_sucursal " +
        "INNER JOIN datos_usuarios dus ON dus.id_empresa = ce.id_empresa " +
        "INNER JOIN datos_usuario du ON du.id_dato_control = dus.id_ctrol_usuario " +
        "WHERE ce.id_empresa = ? AND du.status_de_usuario = ? " +
        "AND ce.estatus_empresa = ? AND csc.estatus_suc = ?";

    /**
     * Obtiene el mapa de roles disponibles en el sistema.
     * @return Map con nombre del rol como clave y ID como valor, null si hay error
     */
    public static Map<String, Integer> obtenerRoles() {
        Map<String, Integer> roles = new TreeMap<>();
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_ROLES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int idRol = rs.getInt(1);
                String nombreRol = rs.getString(2);
                roles.put(nombreRol, idRol);
            }
            
            if (roles.isEmpty()) {
                LOGGER.warning("No se encontraron roles en el sistema");
            }
            
            return roles.isEmpty() ? null : roles;
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al obtener roles", ex);
            return null;
        }
    }

    /**
     * Obtiene la lista de menús disponibles en el sistema.
     * @return Map con nombre del menú como clave y ID como valor, null si hay error
     */
    public static Map<String, Integer> listaMenu() {
        Map<String, Integer> menus = new TreeMap<>();
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_MENU);
             ResultSet rs = stmt.executeQuery()) {
            
            return obtenerMapaStringInteger(rs, menus);
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al obtener menús", ex);
            return null;
        }
    }

    /**
     * Método auxiliar para convertir ResultSet a Map<String, Integer>.
     */
    private static Map<String, Integer> obtenerMapaStringInteger(ResultSet rs, Map<String, Integer> mapa) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt(1);
            String nombre = rs.getString(2);
            mapa.put(nombre, id);
        }
        
        if (mapa.isEmpty()) {
            LOGGER.warning("No se encontraron registros");
            return null;
        }
        
        return mapa;
    }

    /**
     * Obtiene la lista de submenús disponibles en el sistema.
     * @return Map con nombre del submenú como clave y ID como valor, null si hay error
     */
    public static Map<String, Integer> listaSubmenu() {
        Map<String, Integer> submenus = new TreeMap<>();
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_SUBMENU);
             ResultSet rs = stmt.executeQuery()) {
            
            return obtenerMapaStringInteger(rs, submenus);
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al obtener submenús", ex);
            return null;
        }
    }

    /**
     * Método para manejar excepciones SQL de forma centralizada.
     */
    private static void manejarExcepcionSQL(String mensaje, SQLException ex) {
        LOGGER.log(Level.SEVERE, mensaje, ex);
        String errorMsg = mensaje + ": " + ex.getLocalizedMessage();
        System.err.println(errorMsg);
        showInternalMessageDialog(CentroPrincipal.jDesktopPane1, errorMsg, "Error de Base de Datos", INFORMATION_MESSAGE);
    }
    
    /**
     * Obtiene la lista de empresas disponibles en el sistema.
     * @return Map con razón social como clave y ID como valor, null si hay error
     */
    public static Map<String, Long> listaEmpresa() {
        Map<String, Long> empresas = new TreeMap<>();
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_EMPRESA);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                long idEmpresa = rs.getLong(1);
                String razonSocial = rs.getString(2);
                empresas.put(razonSocial, idEmpresa);
            }
            
            if (empresas.isEmpty()) {
                LOGGER.warning("No se encontraron empresas registradas");
                return null;
            }
            
            return empresas;
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al obtener empresas", ex);
            return null;
        }
    }

    /**
     * Valida si un nombre de usuario ya existe en el sistema.
     * @param nombreUsuario Nombre de usuario a validar
     * @return true si el usuario existe, false si no existe o hay error
     */
    public static boolean validarNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            LOGGER.warning("El nombre de usuario está vacío");
            return false;
        }
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_NOMBRE_USUARIO)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                boolean existe = rs.next();
                String usuarioEncontrado = existe ? rs.getString(1) : null;
                
                LOGGER.info(String.format("Validación de usuario '%s': %s", 
                    nombreUsuario, existe ? "existe" : "no existe"));
                    
                return nombreUsuario.equals(usuarioEncontrado);
            }
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al validar nombre de usuario", ex);
            return false;
        }
    }

    /**
     * Valida si un número de empleado ya existe en el sistema.
     * @param numeroEmpleado Número de empleado a validar
     * @return true si el número existe, false si no existe o hay error
     */
    public static boolean validarNumeroEmpleado(String numeroEmpleado) {
        if (numeroEmpleado == null || numeroEmpleado.isBlank()) {
            LOGGER.warning("El número de empleado está vacío");
            return false;
        }
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_NUMERO_EMPLEADO)) {
            
            stmt.setString(1, numeroEmpleado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                String numeroEncontrado = null;
                if (rs.next()) {
                    numeroEncontrado = rs.getString(1);
                }
                
                LOGGER.info(String.format("Validación de empleado '%s': %s", 
                    numeroEmpleado, numeroEncontrado != null ? "existe" : "no existe"));
                    
                return numeroEmpleado.equals(numeroEncontrado);
            }
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al validar número de empleado", ex);
            return false;
        }
    }

    /**
     * Clase interna para encapsular los datos del usuario.
     */
    public static class DatosUsuario {
        public final String nombre;
        public final String apellidoPaterno;
        public final String apellidoMaterno;
        public final String direccion;
        public final String telefono;
        public final String celular;
        public final String email;
        public final BufferedImage fotografia;
        public final String numeroEmpleado;
        public final long idEmpresa;
        public final String nombreUsuario;
        public final String clave;
        public final boolean activo;
        public final int idRol;
        public final long idSucursal;
        public final EnumMap<DPFPFingerIndex, DPFPTemplate> huellas;
        public final String rfid;
        
        public DatosUsuario(String nombre, String apellidoPaterno, String apellidoMaterno,
                           String direccion, String telefono, String celular, String email,
                           BufferedImage fotografia, String numeroEmpleado, long idEmpresa,
                           String nombreUsuario, String clave, boolean activo, int idRol,
                           long idSucursal, EnumMap<DPFPFingerIndex, DPFPTemplate> huellas,
                           String rfid) {
            this.nombre = nombre;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.direccion = direccion;
            this.telefono = telefono;
            this.celular = celular;
            this.email = email;
            this.fotografia = fotografia;
            this.numeroEmpleado = numeroEmpleado;
            this.idEmpresa = idEmpresa;
            this.nombreUsuario = nombreUsuario;
            this.clave = clave;
            this.activo = activo;
            this.idRol = idRol;
            this.idSucursal = idSucursal;
            this.huellas = huellas;
            this.rfid = rfid;
        }
    }
    
    /**
     * Crea un usuario completo en el sistema con todos sus datos y permisos.
     * @param datos Objeto que encapsula todos los datos del usuario
     * @return true si el usuario se creó exitosamente, false en caso contrario
     */
    public static boolean crearUsuario(DatosUsuario datos) {
        LOGGER.info(String.format("Iniciando creación de usuario con rol: %d", datos.idRol));
        
        // Validar que el rol esté en el rango permitido
        if (datos.idRol < 2 || datos.idRol > 10) {
            LOGGER.warning(String.format("Rol no válido: %d", datos.idRol));
            return false;
        }
        
        try {
            // 1. Insertar datos personales del usuario
            if (!insertarDatosPersonales(datos)) {
                LOGGER.severe("Error al insertar datos personales del usuario");
                return false;
            }
            LOGGER.info("Datos personales insertados correctamente");
            
            // 2. Insertar datos de autenticación
            if (!insertarDatosAutenticacion(datos)) {
                LOGGER.severe("Error al insertar datos de autenticación");
                return false;
            }
            LOGGER.info("Datos de autenticación insertados correctamente");
            
            // 3. Obtener ID del usuario recién creado
            int idUsuario = obtenerIdUsuario(datos.numeroEmpleado);
            if (idUsuario == 0) {
                LOGGER.severe("Error al obtener el ID del usuario creado");
                return false;
            }
            LOGGER.info(String.format("ID de usuario obtenido: %d", idUsuario));
            
            // 4. Guardar huellas dactilares
            if (!guardarHuellasDactilares(idUsuario, datos.huellas)) {
                LOGGER.severe("Error al guardar huellas dactilares");
                return false;
            }
            LOGGER.info("Huellas dactilares guardadas correctamente");
            
            // 5. Asignar permisos de menú
            if (!asignarPermisosMenu(idUsuario, datos.idRol)) {
                LOGGER.severe("Error al asignar permisos de menú");
                return false;
            }
            LOGGER.info("Permisos de menú asignados correctamente");
            
            // 6. Asignar permisos de submenú
            asignarPermisosSubmenu(datos.idRol, idUsuario);
            LOGGER.info("Permisos de submenú asignados correctamente");
            
            LOGGER.info(String.format("Usuario '%s' creado exitosamente con ID: %d", 
                datos.nombreUsuario, idUsuario));
            return true;
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error inesperado al crear usuario", ex);
            return false;
        }
    }

    /**
     * Inserta los datos personales del usuario en la base de datos.
     */
    private static boolean insertarDatosPersonales(DatosUsuario datos) {
        LocalDateTime fechaActual = LocalDateTime.now();
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(datos.fotografia, "jpg", baos);
            
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                 Connection conn = obtenerConexion();
                 PreparedStatement stmt = conn.prepareStatement(INSERTAR_DATOS_USUARIO)) {
                
                stmt.setString(1, datos.nombre);
                stmt.setString(2, datos.apellidoPaterno);
                stmt.setString(3, datos.apellidoMaterno);
                stmt.setString(4, datos.direccion);
                stmt.setString(5, datos.telefono);
                stmt.setString(6, datos.celular);
                stmt.setString(7, datos.email);
                stmt.setBinaryStream(8, bais, bais.available());
                stmt.setTimestamp(9, Timestamp.valueOf(fechaActual));
                stmt.setString(10, datos.numeroEmpleado);
                stmt.setLong(11, datos.idEmpresa);
                stmt.setLong(12, datos.idSucursal);
                stmt.setString(13, datos.rfid);
                
                int filasAfectadas = stmt.executeUpdate();
                conn.commit();
                
                return filasAfectadas == 1;
            }
            
        } catch (SQLException | IOException ex) {
            manejarExcepcionSQL("Error al insertar datos personales", 
                ex instanceof SQLException ? (SQLException) ex : new SQLException(ex));
            return false;
        }
    }

    /**
     * Inserta los datos de autenticación del usuario.
     */
    private static boolean insertarDatosAutenticacion(DatosUsuario datos) {
        String claveEncriptada = getHash(datos.clave, 5);
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERTAR_USUARIO_LOCAL)) {
            
            stmt.setString(1, datos.nombreUsuario);
            stmt.setString(2, claveEncriptada);
            stmt.setBoolean(3, datos.activo);
            stmt.setLong(4, datos.idRol);
            
            int filasAfectadas = stmt.executeUpdate();
            conn.commit();
            
            return filasAfectadas == 1;
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al insertar datos de autenticación", ex);
            return false;
        }
    }

    /**
     * Guarda las huellas dactilares del usuario en la base de datos.
     */
    private static boolean guardarHuellasDactilares(int idUsuario, EnumMap<DPFPFingerIndex, DPFPTemplate> huellas) {
        LocalDateTime fechaActual = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(fechaActual);
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERTAR_HUELLA)) {
            
            for (Map.Entry<DPFPFingerIndex, DPFPTemplate> entrada : huellas.entrySet()) {
                try {
                    String identificadorDedo = entrada.getKey().toString();
                    byte[] datosHuella = entrada.getValue().serialize();
                    
                    stmt.setInt(1, idUsuario);
                    stmt.setString(2, identificadorDedo);
                    stmt.setBinaryStream(3, new ByteArrayInputStream(datosHuella), datosHuella.length);
                    stmt.setTimestamp(4, timestamp);
                    stmt.addBatch();
                    
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al procesar huella: " + entrada.getKey(), ex);
                }
            }
            
            int[] resultados = stmt.executeBatch();
            conn.commit();
            
            return validarResultadosBatch(resultados);
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al guardar huellas dactilares", ex);
            return false;
        }
    }

    /**
     * Valida los resultados de una operación batch.
     */
    private static boolean validarResultadosBatch(int[] resultados) {
        boolean exito = true;
        int exitosos = 0;
        int fallidos = 0;
        
        for (int resultado : resultados) {
            if (resultado >= 0) {
                exitosos++;
                LOGGER.fine("Operación exitosa: " + resultado + " filas afectadas");
            } else if (resultado == Statement.SUCCESS_NO_INFO) {
                exitosos++;
                LOGGER.fine("Operación exitosa: número de filas no disponible");
            } else if (resultado == Statement.EXECUTE_FAILED) {
                fallidos++;
                exito = false;
                LOGGER.warning("Operación fallida en batch");
            }
        }
        
        LOGGER.info(String.format("Resultado batch: %d exitosos, %d fallidos", exitosos, fallidos));
        return exito;
    }


    /**
     * Obtiene el ID del usuario basado en su número de empleado.
     */
    private static int obtenerIdUsuario(String numeroEmpleado) {
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_ID_EMPLEADO)) {
            
            stmt.setString(1, numeroEmpleado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
            return 0;
            
        } catch (SQLException ex) {
            manejarExcepcionSQL("Error al obtener ID de usuario", ex);
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