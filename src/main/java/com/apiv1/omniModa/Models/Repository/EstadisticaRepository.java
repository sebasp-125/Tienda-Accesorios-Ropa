package com.apiv1.omniModa.Models.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apiv1.omniModa.Models.Entity.Ventas;

public interface EstadisticaRepository extends JpaRepository<Ventas, Integer> {

    // =========================================================
    // PRODUCTOS MÁS VENDIDOS
    // =========================================================

    @Query("""
                SELECT
                    d.producto.nombre,
                    d.producto.codigo,
                    SUM(d.cantidad)
                FROM Detalle_venta d
                JOIN d.venta v
                JOIN v.cliente c
                WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin
                AND (:tipoCliente IS NULL OR c.tipoCliente.tipo = :tipoCliente)
                GROUP BY d.producto.codigo, d.producto.nombre
                ORDER BY SUM(d.cantidad) DESC
            """)
    List<Object[]> obtenerProductosMasVendidos(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("tipoCliente") String tipoCliente);

    // =========================================================
    // CLIENTES FRECUENTES
    // =========================================================

    @Query("""
                SELECT
                    c.nombreCompleto,
                    c.documento,
                    COUNT(v.idVentas)
                FROM Ventas v
                JOIN v.cliente c
                WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin
                AND (:tipoCliente IS NULL OR c.tipoCliente.tipo = :tipoCliente)
                GROUP BY c.documento, c.nombreCompleto
                ORDER BY COUNT(v.idVentas) DESC
            """)
    List<Object[]> obtenerClientesFrecuentes(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("tipoCliente") String tipoCliente);

    // =========================================================
    // INGRESOS POR MES
    // =========================================================

    @Query("""
                SELECT
                    FUNCTION('MONTH', v.fecha),
                    FUNCTION('YEAR', v.fecha),
                    SUM(v.total)
                FROM Ventas v
                JOIN v.cliente c
                WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin
                AND (:tipoCliente IS NULL OR c.tipoCliente.tipo = :tipoCliente)
                GROUP BY
                    FUNCTION('YEAR', v.fecha),
                    FUNCTION('MONTH', v.fecha)
                ORDER BY
                    FUNCTION('YEAR', v.fecha),
                    FUNCTION('MONTH', v.fecha)
            """)
    List<Object[]> obtenerIngresosPorMes(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("tipoCliente") String tipoCliente);

    // =========================================================
    // INGRESOS POR TIPO DE CLIENTE
    // =========================================================

    @Query("""
                SELECT
                    c.tipoCliente.tipo,
                    SUM(v.total)
                FROM Ventas v
                JOIN v.cliente c
                WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin
                AND (:tipoCliente IS NULL OR c.tipoCliente.tipo = :tipoCliente)
                GROUP BY c.tipoCliente.tipo
                ORDER BY SUM(v.total) DESC
            """)
    List<Object[]> obtenerIngresosPorTipoCliente(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("tipoCliente") String tipoCliente);
}