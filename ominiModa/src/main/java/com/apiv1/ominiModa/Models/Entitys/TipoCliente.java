package com.apiv1.ominiModa.Models.Entitys;

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
    @Column(name = "idTipoCliente")
    private Integer idTipoCliente;

    @Column(name = "Tipo", nullable = false, length = 50)
    private String tipo;

    public TipoCliente() {
    }

    public TipoCliente(Integer idTipoCliente, String tipo) {
        this.idTipoCliente = idTipoCliente;
        this.tipo = tipo;
    }

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
