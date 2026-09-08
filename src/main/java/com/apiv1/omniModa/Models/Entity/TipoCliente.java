package com.apiv1.omniModa.Models.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_cliente")
public class TipoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoCliente")
    private Integer idTipoCliente;

    @Column(name = "Tipo", nullable = false, length = 40)
    private String tipo;

    // Constructor vacío
    public TipoCliente() {
    }

    // Constructor
    public TipoCliente(String tipo) {
        this.tipo = tipo;
    }

    // Getters y Setters

    public Integer getIdTipoCliente() {
        return idTipoCliente;
    }

    public void setIdTipoCliente(Integer idTipoCliente) {
        this.idTipoCliente = idTipoCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}