package com.apiv1.omniModa.Controllers.Inventario;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apiv1.omniModa.Models.Service.ProductoService;

@Controller
public class InventoryController {

    private final ProductoService productoService;

    public InventoryController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/inventario")
    public String inventario(Model model) {

        model.addAttribute(
                "productos",
                productoService.listarInventario());

        model.addAttribute(
                "totalProductos",
                productoService.listarProductos().size());

        model.addAttribute(
                "productosStockBajo",
                productoService.contarProductosStockBajo());

        model.addAttribute(
                "productosAgotados",
                productoService.listarProductos()
                        .stream()
                        .filter(p -> p.getStockDisponible() != null
                                && p.getStockDisponible() == 0)
                        .count());

        model.addAttribute(
                "productosDisponibles",
                productoService.listarProductos()
                        .stream()
                        .filter(p -> p.getStockDisponible() != null
                                && p.getStockDisponible() > 5)
                        .count());

        return "inventory/inventario";
    }
}