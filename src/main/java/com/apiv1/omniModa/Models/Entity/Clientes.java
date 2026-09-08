package com.apiv1.omniModa.Models.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Clientes {

    @Id
    @Column(name = "Documento", nullable = false, length = 20)
    private String documento;

    @Column(name = "Nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "Correo", nullable = false, length = 40)
    private String correo;

    @Column(name = "Telefono", nullable = false, length = 20)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;

    // Constructor vacío
    public Clientes() {
    }

    // Constructor
    public Clientes(
            String documento,
            String nombreCompleto,
            String correo,
            String telefono,
            TipoCliente tipoCliente) {

        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
    }

    // Getters y Setters

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}