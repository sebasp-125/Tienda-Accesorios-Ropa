
package com.apiv1.omniModa.Controllers.Productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import com.apiv1.omniModa.Models.Entity.Categorias;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.CategoriaRepository;
import com.apiv1.omniModa.Models.Service.ProductoService;

@Controller
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ProductoController(
            ProductoService productoService,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository) {

        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    // LISTAR PRODUCTOS
    @GetMapping("/productos")
    public String productosIndex(Model model) {

        model.addAttribute(
                "productos",
                productoService.listarProductos());

        return "product/productos";
    }

    // MOSTRAR FORMULARIO NUEVO PRODUCTO
    @GetMapping("/productos/nuevo")
    public String nuevoProducto(Model model) {

        model.addAttribute(
                "producto",
                new Productos());

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll());

        return "product/nuevo_producto";
    }

    // GUARDAR PRODUCTO
    @PostMapping("/productos/guardar")
    public String guardarProducto(
            Productos producto,
            @RequestParam("categoriaId") Integer categoriaId) {

        Categorias categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        producto.setCategoria(categoria);

        productoService.guardarProducto(producto);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/productos";
    }

    // MOSTRAR FORMULARIO ACTUALIZAR PRODUCTO
    @GetMapping("/productos/actualizar/{codigo}")
    public String actualizarProducto(
            @PathVariable String codigo,
            Model model) {

        Productos producto = productoService.buscarPorId(codigo);

        if (producto == null) {
            return "redirect:/productos";
        }

        model.addAttribute(
                "producto",
                producto);

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll());

        return "product/actualizar_producto";
    }

    // ACTUALIZAR PRODUCTO
    @PostMapping("/productos/actualizar")
    public String guardarActualizacion(
            Productos producto,
            @RequestParam("categoriaId") Integer categoriaId) {

        Categorias categoria = categoriaRepository
                .findById(categoriaId)
                .orElse(null);

        producto.setCategoria(categoria);

        productoService.guardarProducto(producto);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/productos";
    }

    // Para consumir el consecutivo con la categoria CZ - 001
    @GetMapping("/productos/siguiente-codigo")
    @ResponseBody
    public String siguienteCodigo() {

        List<String> codigos = productoRepository.obtenerCodigos();

        int siguiente = 1;

        for (String codigo : codigos) {

            try {

                String numeroCodigo = codigo.substring(
                        codigo.lastIndexOf("-") + 1);

                int numero = Integer.parseInt(numeroCodigo);

                if (numero >= siguiente) {
                    siguiente = numero + 1;
                }

            } catch (Exception e) {
                // Ignora códigos con formato inválido
            }
        }

        return String.format("%03d", siguiente);
    }

    // ELIMINAR PRODUCTO
    @PostMapping("/productos/eliminar/{codigo}")
    public String eliminarProducto(
            @PathVariable String codigo) {

        productoService.eliminarProducto(codigo);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/productos";
    }
}