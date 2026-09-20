package com.apiv1.omniModa.Models.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVentas")
    private Integer idVentas;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Column(name = "Total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "Cliente_documento", referencedColumnName = "Documento")
    private Clientes cliente;

    @ManyToOne
    @JoinColumn(name = "Estado_id", referencedColumnName = "idEstado")
    private Estados estado;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle_venta> detalles = new ArrayList<>();

    public Ventas() {
    }

    public Ventas(Integer idVentas, LocalDate fecha, Double total, Clientes cliente, Estados estado) {
        this.idVentas = idVentas;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.estado = estado;
    }

    public Integer getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public List<Detalle_venta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle_venta> detalles) {
        this.detalles = detalles;
    }

    public void addDetalle(Detalle_venta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void removeDetalle(Detalle_venta detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }
}
