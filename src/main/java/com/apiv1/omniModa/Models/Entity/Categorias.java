package com.apiv1.omniModa.Models.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoCategoria")
    private Integer idTipoCategoria;

    @Column(name = "Tipo", nullable = false, length = 40)
    private String tipo;

    public Categorias() {
    }

    public Categorias(Integer idTipoCategoria, String tipo) {
        this.idTipoCategoria = idTipoCategoria;
        this.tipo = tipo;
    }

    public Integer getIdTipoCategoria() {
        return idTipoCategoria;
    }

    public void setIdTipoCategoria(Integer idTipoCategoria) {
        this.idTipoCategoria = idTipoCategoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
