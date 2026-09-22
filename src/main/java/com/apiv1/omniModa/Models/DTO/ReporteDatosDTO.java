package com.apiv1.omniModa.Models.DTO;

import java.util.List;

import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.Ventas;

/**
 * DTO para concentrar los datos de métricas y auditoría
 * requeridos en los reportes administrativos en PDF.
 */
public class ReporteDatosDTO {

    private String adminNombre;
    private String tipoReporte; // "general", "ventas", "inventario"
    private long totalClientes;
    private long totalProductos;
    private long totalProveedores;
    private long totalUsuarios;
    private long totalVentas;
    private double totalIngresos;
    private long ventasPagadas;
    private long ventasPendientes;
    private long ventasCanceladas;
    private List<Productos> productosBajoStock;
    private List<Productos> todosProductos;
    private List<Ventas> ventasRecientes;

    public ReporteDatosDTO() {
    }

    public String getAdminNombre() {
        return adminNombre;
    }

    public void setAdminNombre(String adminNombre) {
        this.adminNombre = adminNombre;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(long totalClientes) {
        this.totalClientes = totalClientes;
    }

    public long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public long getTotalProveedores() {
        return totalProveedores;
    }

    public void setTotalProveedores(long totalProveedores) {
        this.totalProveedores = totalProveedores;
    }

    public long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public long getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(long totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public long getVentasPagadas() {
        return ventasPagadas;
    }

    public void setVentasPagadas(long ventasPagadas) {
        this.ventasPagadas = ventasPagadas;
    }

    public long getVentasPendientes() {
        return ventasPendientes;
    }

    public void setVentasPendientes(long ventasPendientes) {
        this.ventasPendientes = ventasPendientes;
    }

    public long getVentasCanceladas() {
        return ventasCanceladas;
    }

    public void setVentasCanceladas(long ventasCanceladas) {
        this.ventasCanceladas = ventasCanceladas;
    }

    public List<Productos> getProductosBajoStock() {
        return productosBajoStock;
    }

    public void setProductosBajoStock(List<Productos> productosBajoStock) {
        this.productosBajoStock = productosBajoStock;
    }

    public List<Productos> getTodosProductos() {
        return todosProductos;
    }

    public void setTodosProductos(List<Productos> todosProductos) {
        this.todosProductos = todosProductos;
    }

    public List<Ventas> getVentasRecientes() {
        return ventasRecientes;
    }

    public void setVentasRecientes(List<Ventas> ventasRecientes) {
        this.ventasRecientes = ventasRecientes;
    }
}
