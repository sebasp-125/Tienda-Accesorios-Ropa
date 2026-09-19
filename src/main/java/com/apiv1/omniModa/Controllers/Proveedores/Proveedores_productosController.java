package com.apiv1.omniModa.Controllers.Proveedores;

import com.apiv1.omniModa.Models.DTO.ProductoProveedorDTO;
import com.apiv1.omniModa.Models.Entity.Proveedores_productos;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;
import com.apiv1.omniModa.Models.Service.Proveedores_productosService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proveedores-productos")
public class Proveedores_productosController {

    private final Proveedores_productosService proveedoresProductosService;
    private final ProveedorRepository proveedoresRepository;
    private final ProductoRepository productosRepository;

    public Proveedores_productosController(
            Proveedores_productosService proveedoresProductosService,
            ProveedorRepository proveedoresRepository,
            ProductoRepository productosRepository) {

        this.proveedoresProductosService = proveedoresProductosService;
        this.proveedoresRepository = proveedoresRepository;
        this.productosRepository = productosRepository;
    }

    @GetMapping
    public String mostrarVista(Model model) {

        model.addAttribute(
                "proveedores",
                proveedoresRepository.findAll());

        model.addAttribute(
                "productos",
                productosRepository.findAll());

        return "product/proveedores_productos";
    }

    @PostMapping("/guardar")
    public String guardarAsociaciones(
            @RequestParam("proveedorNit") String proveedorNit,
            @RequestParam(value = "productosIds", required = false) List<String> productosIds) {

        proveedoresProductosService.guardarAsociaciones(
                proveedorNit,
                productosIds);

        return "redirect:/proveedores-productos?guardado";
    }

    @GetMapping("/consultar/{nit}")
    @ResponseBody
    public ResponseEntity<List<ProductoProveedorDTO>> consultarProductos(
            @PathVariable("nit") String nit) {

        List<Proveedores_productos> asociaciones = proveedoresProductosService
                .obtenerProductosPorProveedor(nit);

        List<ProductoProveedorDTO> productos = asociaciones.stream()
                .map(asociacion -> {

                    var producto = asociacion.getProducto();

                    String categoria = producto.getCategoria() != null
                            ? producto.getCategoria().getTipo()
                            : "Sin categoría";

                    return new ProductoProveedorDTO(
                            producto.getCodigo(),
                            producto.getNombre(),
                            producto.getTalla(),
                            producto.getColor(),
                            categoria);
                })
                .toList();

        return ResponseEntity.ok(productos);
    }
}