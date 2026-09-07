package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedores {

    @Id
    @Column(name = "NIT", length = 40)
    private String nit;

    @Column(name = "Nombre", nullable = false, length = 40)
    private String nombre;

    @Column(name = "Correo", length = 40)
    private String correo;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    public Proveedores() {
    }

    public Proveedores(String nit, String nombre, String correo, String telefono) {
        this.nit = nit;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
