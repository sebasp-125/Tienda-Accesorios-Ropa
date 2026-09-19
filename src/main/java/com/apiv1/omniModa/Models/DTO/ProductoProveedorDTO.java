package com.apiv1.omniModa.Models.DTO;

public class ProductoProveedorDTO {

    private String codigo;
    private String nombre;
    private String talla;
    private String color;
    private String categoria;

    public ProductoProveedorDTO() {
    }

    public ProductoProveedorDTO(
            String codigo,
            String nombre,
            String talla,
            String color,
            String categoria) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.talla = talla;
        this.color = color;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}