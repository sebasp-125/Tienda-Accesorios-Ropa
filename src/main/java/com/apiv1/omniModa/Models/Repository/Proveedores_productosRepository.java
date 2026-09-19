package com.apiv1.omniModa.Models.Repository;

import com.apiv1.omniModa.Models.Entity.Proveedores_productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Proveedores_productosRepository
        extends JpaRepository<Proveedores_productos, Integer> {

    List<Proveedores_productos> findByProveedor_Nit(String nitProveedor);

    void deleteByProveedor_Nit(String nitProveedor);

}