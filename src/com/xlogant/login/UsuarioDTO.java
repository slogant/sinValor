package com.xlogant.login;

/**
 * Data Transfer Object (DTO) para encapsular la información
 * de un usuario autenticado.
 */
public class UsuarioDTO {
    private final int id;
    private final String nombreUsuario;
    private final String nombreCompleto;
    private final String rol;
    private final long idEmpresa;
    private final String nombreSucursal;

    public UsuarioDTO(int id, String nombreUsuario, String nombreCompleto, String rol, long idEmpresa, String nombreSucursal) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.idEmpresa = idEmpresa;
        this.nombreSucursal = nombreSucursal;
    }

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public long getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }
    //</editor-fold>
}