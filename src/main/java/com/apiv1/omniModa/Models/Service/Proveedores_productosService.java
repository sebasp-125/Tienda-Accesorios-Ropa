package com.apiv1.omniModa.Models.Service;

import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.Proveedores;
import com.apiv1.omniModa.Models.Entity.Proveedores_productos;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;
import com.apiv1.omniModa.Models.Repository.Proveedores_productosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Proveedores_productosService {

    private final Proveedores_productosRepository proveedoresProductosRepository;
    private final ProveedorRepository proveedoresRepository;
    private final ProductoRepository productosRepository;

    public Proveedores_productosService(
            Proveedores_productosRepository proveedoresProductosRepository,
            ProveedorRepository proveedoresRepository,
            ProductoRepository productosRepository) {

        this.proveedoresProductosRepository = proveedoresProductosRepository;
        this.proveedoresRepository = proveedoresRepository;
        this.productosRepository = productosRepository;
    }

    public List<Proveedores_productos> obtenerTodos() {
        return proveedoresProductosRepository.findAll();
    }

    public List<Proveedores_productos> obtenerProductosPorProveedor(String nitProveedor) {
        return proveedoresProductosRepository.findByProveedor_Nit(nitProveedor);
    }

    @Transactional
    public void guardarAsociaciones(
            String nitProveedor,
            List<String> codigosProductos) {

        Proveedores proveedor = proveedoresRepository
                .findById(nitProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedoresProductosRepository.deleteByProveedor_Nit(nitProveedor);

        if (codigosProductos == null || codigosProductos.isEmpty()) {
            return;
        }

        List<Proveedores_productos> asociaciones = codigosProductos
                .stream()
                .map(codigo -> {

                    Productos producto = productosRepository
                            .findById(codigo)
                            .orElseThrow(() -> new RuntimeException(
                                    "Producto no encontrado: " + codigo));

                    Proveedores_productos asociacion = new Proveedores_productos();

                    asociacion.setProveedor(proveedor);
                    asociacion.setProducto(producto);

                    return asociacion;
                })
                .toList();

        proveedoresProductosRepository.saveAll(asociaciones);
    }
}