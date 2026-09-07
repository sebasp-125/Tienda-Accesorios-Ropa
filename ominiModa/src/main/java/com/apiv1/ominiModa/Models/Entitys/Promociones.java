package com.apiv1.ominiModa.Models.Entitys;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "promociones")
public class Promociones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPromocion")
    private Integer idPromocion;

    @Column(name = "Titulo", nullable = false, length = 70)
    private String titulo;

    @Column(name = "Descripcion", length = 200)
    private String descripcion;

    @Column(name = "Porcentaje_descuento")
    private Double porcentajeDescuento;

    @Column(name = "Fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "Fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "Estado", length = 20)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "Tipo_cliente", referencedColumnName = "idTipoCliente")
    private TipoCliente tipoCliente;

    @ManyToOne
    @JoinColumn(name = "Categoria_id", referencedColumnName = "idTipoCategoria")
    private Categorias categoria;

    public Promociones() {
    }

    public Promociones(Integer idPromocion, String titulo, String descripcion, Double porcentajeDescuento,
                       LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado,
                       TipoCliente tipoCliente, Categorias categoria) {
        this.idPromocion = idPromocion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.tipoCliente = tipoCliente;
        this.categoria = categoria;
    }

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Integer idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }
}
