package com.apiv1.omniModa.Models.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Proveedores;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedores> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedores guardarProveedor(Proveedores proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedores buscarPorNIT(String nit) {
        return proveedorRepository.findById(nit).orElse(null);
    }

    public void eliminarProveedor(String nit) {
        proveedorRepository.deleteById(nit);
    }
}