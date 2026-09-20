package com.apiv1.omniModa.Models.DTO;

import java.time.LocalDate;
import java.util.List;

public class VentaDetalleDTO {

    private Integer idVenta;
    private LocalDate fecha;
    private Double total;
    private String estado;
    private Integer estadoId;
    private String clienteDocumento;
    private String clienteNombre;
    private String clienteCorreo;
    private String clienteTelefono;
    private List<ItemVentaDTO> items;

    public VentaDetalleDTO() {
    }

    public VentaDetalleDTO(Integer idVenta, LocalDate fecha, Double total, String estado, Integer estadoId,
                           String clienteDocumento, String clienteNombre, String clienteCorreo, String clienteTelefono,
                           List<ItemVentaDTO> items) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.estadoId = estadoId;
        this.clienteDocumento = clienteDocumento;
        this.clienteNombre = clienteNombre;
        this.clienteCorreo = clienteCorreo;
        this.clienteTelefono = clienteTelefono;
        this.items = items;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public String getClienteDocumento() {
        return clienteDocumento;
    }

    public void setClienteDocumento(String clienteDocumento) {
        this.clienteDocumento = clienteDocumento;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public void setClienteCorreo(String clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }
}
