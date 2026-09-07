package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class Estados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado")
    private Integer idEstado;

    @Column(name = "Tipo", nullable = false, length = 40)
    private String tipo;

    public Estados() {
    }

    public Estados(Integer idEstado, String tipo) {
        this.idEstado = idEstado;
        this.tipo = tipo;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
