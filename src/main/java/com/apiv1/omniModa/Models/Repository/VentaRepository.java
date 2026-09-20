package com.apiv1.omniModa.Models.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apiv1.omniModa.Models.Entity.Ventas;

@Repository
public interface VentaRepository extends JpaRepository<Ventas, Integer> {

    List<Ventas> findAllByOrderByFechaDescIdVentasDesc();

    List<Ventas> findByClienteDocumentoOrderByFechaDescIdVentasDesc(String documento);

    List<Ventas> findTop5ByOrderByFechaDescIdVentasDesc();

    long countByEstadoTipoIgnoreCase(String tipo);

    @Query("SELECT COALESCE(SUM(v.total), 0.0) FROM Ventas v WHERE UPPER(v.estado.tipo) = 'PAGADA'")
    Double sumTotalVentasPagadas();

    @Query("SELECT COALESCE(SUM(v.total), 0.0) FROM Ventas v")
    Double sumTotalVentas();

    @Query("SELECT DISTINCT v FROM Ventas v " +
           "LEFT JOIN v.cliente c " +
           "LEFT JOIN v.detalles d " +
           "LEFT JOIN d.producto p " +
           "WHERE (:cliente IS NULL OR :cliente = '' OR LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :cliente, '%')) OR LOWER(c.documento) LIKE LOWER(CONCAT('%', :cliente, '%'))) " +
           "AND (:producto IS NULL OR :producto = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :producto, '%')) OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :producto, '%'))) " +
           "AND (:fecha IS NULL OR v.fecha = :fecha) " +
           "AND (:estadoId IS NULL OR v.estado.idEstado = :estadoId) " +
           "ORDER BY v.fecha DESC, v.idVentas DESC")
    List<Ventas> filtrarVentas(
            @Param("cliente") String cliente,
            @Param("producto") String producto,
            @Param("fecha") LocalDate fecha,
            @Param("estadoId") Integer estadoId);
}
