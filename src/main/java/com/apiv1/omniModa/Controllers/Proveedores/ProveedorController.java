package com.apiv1.omniModa.Controllers.Proveedores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.apiv1.omniModa.Models.Entity.Proveedores;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;
import com.apiv1.omniModa.Models.Service.ProveedorService;

@Controller
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ProveedorRepository proveedorRepository;

    public ProveedorController(
            ProveedorService proveedorService,
            ProveedorRepository proveedorRepository) {

        this.proveedorService = proveedorService;
        this.proveedorRepository = proveedorRepository;
    }

    // LISTAR PROVEEDORES
    @GetMapping("/proveedores")
    public String proveedoresIndex(Model model) {

        model.addAttribute("proveedores", proveedorService.listarProveedores());

        return "provider/proveedores";
    }

    // MOSTRAR FORMULARIO NUEVO PROVEEDOR
    @GetMapping("/proveedores/nuevo")
    public String nuevoProveedor(Model model) {

        model.addAttribute("proveedor", new Proveedores());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        return "provider/nuevo_proveedor";
    }

    // GUARDAR PROVEEDOR
    @PostMapping("/proveedores/guardar")
    public String guardarProveedor(Proveedores proveedor) {

        proveedorService.guardarProveedor(proveedor);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/proveedores";
    }

    // MOSTRAR FORMULARIO ACTUALIZAR PROVEEDOR
    @GetMapping("/proveedores/actualizar/{nit}")
    public String actualizarProveedor(@PathVariable String nit, Model model) {
        Proveedores proveedor = proveedorService.buscarPorNIT(nit);
        if (proveedor == null) {
            return "redirect:/proveedores";
        }
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "provider/actualizar_proveedor";
    }

    // ACTUALIZAR PROVEEDOR
    @PostMapping("/proveedores/actualizar")
    public String guardarActualizacion(Proveedores proveedor) {
        proveedorService.guardarProveedor(proveedor);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/proveedores";
    }

    // ELIMINAR PROVEEDOR
    @PostMapping("/proveedores/eliminar/{nit}")
    public String eliminarProveedor(@PathVariable String nit) {
        proveedorService.eliminarProveedor(nit);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/proveedores";
    }
}