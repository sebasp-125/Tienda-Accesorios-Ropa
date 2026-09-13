package com.apiv1.omniModa.Models.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Productos> listarProductos() {
        return productoRepository.findAll();
    }

    public Productos guardarProducto(Productos producto) {
        return productoRepository.save(producto);
    }

    public Productos buscarPorId(String id) {
        return productoRepository.findById(id).orElse(null);
    }

    public void eliminarProducto(String id) {
        productoRepository.deleteById(id);
    }
}