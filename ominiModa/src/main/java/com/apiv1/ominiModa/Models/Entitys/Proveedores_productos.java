package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores_productos")
public class Proveedores_productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor_producto")
    private Integer idProveedorProducto;

    @ManyToOne
    @JoinColumn(name = "Codigo_producto", referencedColumnName = "Codigo")
    private Productos producto;

    @ManyToOne
    @JoinColumn(name = "NIT_proveedor", referencedColumnName = "NIT")
    private Proveedores proveedor;

    public Proveedores_productos() {
    }

    public Proveedores_productos(Integer idProveedorProducto, Productos producto, Proveedores proveedor) {
        this.idProveedorProducto = idProveedorProducto;
        this.producto = producto;
        this.proveedor = proveedor;
    }

    public Integer getIdProveedorProducto() {
        return idProveedorProducto;
    }

    public void setIdProveedorProducto(Integer idProveedorProducto) {
        this.idProveedorProducto = idProveedorProducto;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }
}
