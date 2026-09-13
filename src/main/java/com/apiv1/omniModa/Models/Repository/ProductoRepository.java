package com.apiv1.omniModa.Models.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apiv1.omniModa.Models.Entity.Productos;

public interface ProductoRepository extends JpaRepository<Productos, String> {

    @Query("SELECT p.codigo FROM Productos p")
    java.util.List<String> obtenerCodigos();

}