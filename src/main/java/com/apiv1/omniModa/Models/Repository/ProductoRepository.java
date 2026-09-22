package com.apiv1.omniModa.Models.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apiv1.omniModa.Models.Entity.Productos;

public interface ProductoRepository extends JpaRepository<Productos, String> {

    @Query("SELECT p.codigo FROM Productos p")
    List<String> obtenerCodigos();

    List<Productos> findByStockDisponibleLessThanEqualOrderByStockDisponibleAsc(
            Integer stockMaximo);

    List<Productos> findAllByOrderByStockDisponibleAsc();

    long countByStockDisponibleLessThanEqual(Integer stockMaximo);
}