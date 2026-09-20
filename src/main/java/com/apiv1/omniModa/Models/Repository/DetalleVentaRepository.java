package com.apiv1.omniModa.Models.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Ventas;

@Repository
public interface DetalleVentaRepository extends JpaRepository<Detalle_venta, Long> {

    List<Detalle_venta> findByVenta(Ventas venta);

    List<Detalle_venta> findByVentaIdVentas(Integer idVentas);

    void deleteByVenta(Ventas venta);
}
