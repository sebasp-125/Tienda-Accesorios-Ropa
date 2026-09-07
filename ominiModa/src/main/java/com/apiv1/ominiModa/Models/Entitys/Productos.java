package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Productos {

    @Id
    @Column(name = "Codigo", length = 40)
    private String codigo;

    @Column(name = "Nombre", nullable = false, length = 40)
    private String nombre;

    @Column(name = "Talla", length = 10)
    private String talla;

    @Column(name = "Color", length = 20)
    private String color;

    @Column(name = "Precio")
    private Double precio;

    @Column(name = "Stock_disponible")
    private Integer stockDisponible;

    @ManyToOne
    @JoinColumn(name = "Categoria_id", referencedColumnName = "idTipoCategoria")
    private Categorias categoria;

    public Productos() {
    }

    public Productos(String codigo, String nombre, String talla, String color, Double precio, Integer stockDisponible, Categorias categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.talla = talla;
        this.color = color;
        this.precio = precio;
        this.stockDisponible = stockDisponible;
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }
}
