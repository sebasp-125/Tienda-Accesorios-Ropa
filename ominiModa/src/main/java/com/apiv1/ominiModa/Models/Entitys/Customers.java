package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Customers {

    @Id
    @Column(name = "Documento", length = 20)
    private String documento;

    @Column(name = "Nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "Correo", nullable = false, length = 40)
    private String correo;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "Tipo_cliente", referencedColumnName = "idTipoCliente")
    private TipoCliente tipoCliente;

    public Customers() {
    }

    public Customers(String documento, String nombreCompleto, String correo, String telefono, TipoCliente tipoCliente) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
    }

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
