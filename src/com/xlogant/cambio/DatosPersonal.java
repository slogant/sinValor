package com.xlogant.cambio;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author antonio
 */
final public class DatosPersonal implements Serializable {

    public DatosPersonal(int id_control, String nombre, String apellidoPat,
            String apellidoMat, String direccion, String telefono, String celular, String email,
            InputStream foto, LocalDateTime fecha, String numEmp,
            String nom_sucursal, String Rfid, boolean estatus, String nuser, String pss) {
        this.id_control = id_control;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.telefono = telefono;
        this.celular = celular;
        this.email = email;
        this.direccion = direccion;
        this.foto = foto;
        this.fecha = fecha;
        this.numEmp = numEmp;
        this.nom_sucursal = nom_sucursal;
        this.Rfid = Rfid;
        this.estatus = estatus;
        this.nuser = nuser;
        this.pss = pss;
    }

    public DatosPersonal(int id_control, String nombre, String apellidoPat, String apellidoMat,
            String direccion, String telefono, String celular, String email,
            InputStream foto, LocalDateTime fecha, String numEmp, String nom_sucursal,
            String Rfid, boolean estatus) {
        this.id_control = id_control;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.telefono = telefono;
        this.celular = celular;
        this.email = email;
        this.direccion = direccion;
        this.foto = foto;
        this.fecha = fecha;
        this.numEmp = numEmp;
        this.nom_sucursal = nom_sucursal;
        this.Rfid = Rfid;
        this.estatus = estatus;
    }

    public DatosPersonal(int id_control, String nombre, String apellidoPat, String apellidoMat,
            String direccion, String telefono, String celular, String email,
            InputStream foto, LocalDateTime fecha, String numEmp, String nom_sucursal, String Rfid) {
        this.id_control = id_control;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.telefono = telefono;
        this.celular = celular;
        this.email = email;
        this.direccion = direccion;
        this.foto = foto;
        this.fecha = fecha;
        this.numEmp = numEmp;
        this.nom_sucursal = nom_sucursal;
        this.Rfid = Rfid;
    }

    public DatosPersonal(int id_control, String nombre, String apellidoPat,
            String apellidoMat, String direccion, String telefono,
            String celular, String email, InputStream foto, LocalDateTime fecha,
            String numEmp, String nom_sucursal, String Rfid, String tpo, boolean estatus,
            String nuser, String pss) {
        this.id_control = id_control;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.email = email;
        this.foto = foto;
        this.fecha = fecha;
        this.numEmp = numEmp;
        this.nom_sucursal = nom_sucursal;
        this.Rfid = Rfid;
        this.tpo = tpo;
        this.estatus = estatus;
        this.nuser = nuser;
        this.pss = pss;
    }

    public int getId_control() {
        return id_control;
    }

    public void setId_control(int id_control) {
        this.id_control = id_control;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNumEmp() {
        return numEmp;
    }

    public void setNumEmp(String numEmp) {
        this.numEmp = numEmp;
    }

    public String getNom_sucursal() {
        return nom_sucursal;
    }

    public void setNom_sucursal(String nom_sucursal) {
        this.nom_sucursal = nom_sucursal;
    }

    public String getRfid() {
        return Rfid;
    }

    public void setRfid(String Rfid) {
        this.Rfid = Rfid;
    }

    public String getTpo() {
        return tpo;
    }

    public void setTpo(String tpo) {
        this.tpo = tpo;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public String getNuser() {
        return nuser;
    }

    public void setNuser(String nuser) {
        this.nuser = nuser;
    }

    public String getPss() {
        return pss;
    }

    public void setPss(String pss) {
        this.pss = pss;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.id_control;
        hash = 71 * hash + Objects.hashCode(this.nombre);
        hash = 71 * hash + Objects.hashCode(this.apellidoPat);
        hash = 71 * hash + Objects.hashCode(this.apellidoMat);
        hash = 71 * hash + Objects.hashCode(this.direccion);
        hash = 71 * hash + Objects.hashCode(this.telefono);
        hash = 71 * hash + Objects.hashCode(this.celular);
        hash = 71 * hash + Objects.hashCode(this.email);
        hash = 71 * hash + Objects.hashCode(this.foto);
        hash = 71 * hash + Objects.hashCode(this.fecha);
        hash = 71 * hash + Objects.hashCode(this.numEmp);
        hash = 71 * hash + Objects.hashCode(this.nom_sucursal);
        hash = 71 * hash + Objects.hashCode(this.Rfid);
        hash = 71 * hash + Objects.hashCode(this.tpo);
        hash = 71 * hash + (this.estatus ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.nuser);
        hash = 71 * hash + Objects.hashCode(this.pss);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DatosPersonal other = (DatosPersonal) obj;
        if (this.id_control != other.id_control) {
            return false;
        }
        if (this.estatus != other.estatus) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellidoPat, other.apellidoPat)) {
            return false;
        }
        if (!Objects.equals(this.apellidoMat, other.apellidoMat)) {
            return false;
        }
        if (!Objects.equals(this.direccion, other.direccion)) {
            return false;
        }
        if (!Objects.equals(this.telefono, other.telefono)) {
            return false;
        }
        if (!Objects.equals(this.celular, other.celular)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.numEmp, other.numEmp)) {
            return false;
        }
        if (!Objects.equals(this.nom_sucursal, other.nom_sucursal)) {
            return false;
        }
        if (!Objects.equals(this.Rfid, other.Rfid)) {
            return false;
        }
        if (!Objects.equals(this.tpo, other.tpo)) {
            return false;
        }
        if (!Objects.equals(this.nuser, other.nuser)) {
            return false;
        }
        if (!Objects.equals(this.pss, other.pss)) {
            return false;
        }
        if (!Objects.equals(this.foto, other.foto)) {
            return false;
        }
        return Objects.equals(this.fecha, other.fecha);
    }

    @Override public String toString() {
        return "DatosPersonal{" + "id_control=" + id_control
                + ", nombre=" + nombre + ", apellidoPat=" + apellidoPat
                + ", apellidoMat=" + apellidoMat + ", direccion=" + direccion
                + ", telefono=" + telefono + ", celular=" + celular
                + ", email=" + email + ", foto=" + foto + ", fecha=" + fecha
                + ", numEmp=" + numEmp + ", nom_sucursal=" + nom_sucursal
                + ", Rfid=" + Rfid + ", tpo=" + tpo + ", estatus=" + estatus
                + ", nuser=" + nuser + ", pss=" + pss + '}';
    }

    /**
     *
     */
    @Serial
    static final private long serialVersionUID = 5151175705629678329L;
    private int id_control;
    private String nombre;
    private String apellidoPat;
    private String apellidoMat;
    private String direccion;
    private String telefono;
    private String celular;
    private String email;
    private InputStream foto;
    private LocalDateTime fecha;
    private String numEmp;
    private String nom_sucursal;
    private String Rfid;
    private String tpo;
    private boolean estatus;
    private String nuser;
    private String pss;

}
