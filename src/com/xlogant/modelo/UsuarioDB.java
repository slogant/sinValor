/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlogant.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author oscar
 */
public class UsuarioDB implements Serializable {

    public UsuarioDB() {
    }

    public UsuarioDB(long id_usuario, String nombre_usuario, String clave_usuario, boolean estatus_usuario, long id_role) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.clave_usuario = clave_usuario;
        this.estatus_usuario = estatus_usuario;
        this.id_role = id_role;
    }
    

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    public boolean isEstatus_usuario() {
        return estatus_usuario;
    }

    public void setEstatus_usuario(boolean estatus_usuario) {
        this.estatus_usuario = estatus_usuario;
    }

    public long getId_role() {
        return id_role;
    }

    public void setId_role(long id_role) {
        this.id_role = id_role;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioDB other = (UsuarioDB) obj;
        if (this.id_usuario != other.id_usuario) {
            return false;
        }
        if (!Objects.equals(this.nombre_usuario, other.nombre_usuario)) {
            return false;
        }
        if (!Objects.equals(this.clave_usuario, other.clave_usuario)) {
            return false;
        }
        if (this.estatus_usuario != other.estatus_usuario) {
            return false;
        }
        return this.id_role == other.id_role;
    }

    @Override
    public String toString() {
        return "UsuarioDB{" + "id_usuario=" + id_usuario + ", nombre_usuario=" + nombre_usuario + ", clave_usuario=" + clave_usuario + ", estatus_usuario=" + estatus_usuario + ", id_role=" + id_role + '}';
    }
    
    @Serial
    private static final long serialVersionUID = -8327757545315661753L;
    private long id_usuario;
    private String nombre_usuario;
    private String clave_usuario;
    private boolean estatus_usuario;
    private long id_role;

}
