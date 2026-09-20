package com.apiv1.omniModa.Controllers.Ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apiv1.omniModa.Models.DTO.ItemVentaDTO;
import com.apiv1.omniModa.Models.DTO.VentaDetalleDTO;
import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Service.ClienteService;
import com.apiv1.omniModa.Models.Service.ProductoService;
import com.apiv1.omniModa.Models.Service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentasController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public VentasController(
            VentaService ventaService,
            ClienteService clienteService,
            ProductoService productoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    // =====================================================
    // LISTADO Y CONSULTA DE VENTAS (CON FILTROS)
    // =====================================================
    @GetMapping
    public String listarVentas(
            @RequestParam(name = "cliente", required = false) String cliente,
            @RequestParam(name = "producto", required = false) String producto,
            @RequestParam(name = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(name = "estadoId", required = false) Integer estadoId,
            Model model) {

        List<Ventas> ventas = ventaService.filtrarVentas(cliente, producto, fecha, estadoId);

        model.addAttribute("ventas", ventas);
        model.addAttribute("totalVentas", ventaService.contarTotalVentas());
        model.addAttribute("totalIngresos", ventaService.calcularTotalIngresos());
        model.addAttribute("ventasPagadas", ventaService.contarPorEstado("PAGADA"));
        model.addAttribute("ventasPendientes", ventaService.contarPorEstado("PENDIENTE"));
        model.addAttribute("estados", ventaService.listarEstados());

        model.addAttribute("filtroCliente", cliente != null ? cliente : "");
        model.addAttribute("filtroProducto", producto != null ? producto : "");
        model.addAttribute("filtroFecha", fecha != null ? fecha.toString() : "");
        model.addAttribute("filtroEstadoId", estadoId);
        model.addAttribute("paginaActual", "ventas");

        return "sale/ventas";
    }

    // =====================================================
    // FORMULARIO NUEVA VENTA
    // =====================================================
    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("estados", ventaService.listarEstados());
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("paginaActual", "ventas");

        return "sale/nueva_venta";
    }

    // =====================================================
    // GUARDAR NUEVA VENTA
    // =====================================================
    @PostMapping("/guardar")
    public String guardarVenta(
            @RequestParam("clienteDocumento") String clienteDocumento,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("estadoId") Integer estadoId,
            @RequestParam("codigos") List<String> codigos,
            @RequestParam("cantidades") List<Integer> cantidades) {

        List<ItemVentaDTO> items = new ArrayList<>();
        if (codigos != null && cantidades != null) {
            for (int i = 0; i < codigos.size(); i++) {
                if (i < cantidades.size() && cantidades.get(i) > 0) {
                    items.add(new ItemVentaDTO(codigos.get(i), null, cantidades.get(i), null, null));
                }
            }
        }

        LocalDate fechaVenta = (fecha != null) ? fecha : LocalDate.now();
        ventaService.registrarVenta(clienteDocumento, fechaVenta, estadoId, items);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/ventas";
    }

    // =====================================================
    // FORMULARIO ACTUALIZAR VENTA
    // =====================================================
    @GetMapping("/actualizar/{id}")
    public String actualizarVentaForm(@PathVariable("id") Integer id, Model model) {
        Ventas venta = ventaService.buscarPorId(id);
        if (venta == null) {
            return "redirect:/ventas";
        }

        List<Detalle_venta> detalles = ventaService.obtenerDetallesVenta(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("estados", ventaService.listarEstados());
        model.addAttribute("paginaActual", "ventas");

        return "sale/actualizar_venta";
    }

    // =====================================================
    // GUARDAR ACTUALIZACIÓN DE VENTA
    // =====================================================
    @PostMapping("/actualizar")
    public String guardarActualizacionVenta(
            @RequestParam("idVentas") Integer idVentas,
            @RequestParam("clienteDocumento") String clienteDocumento,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("estadoId") Integer estadoId,
            @RequestParam(value = "codigos", required = false) List<String> codigos,
            @RequestParam(value = "cantidades", required = false) List<Integer> cantidades) {

        List<ItemVentaDTO> items = new ArrayList<>();
        if (codigos != null && cantidades != null) {
            for (int i = 0; i < codigos.size(); i++) {
                if (i < cantidades.size() && cantidades.get(i) > 0) {
                    items.add(new ItemVentaDTO(codigos.get(i), null, cantidades.get(i), null, null));
                }
            }
        }

        LocalDate fechaVenta = (fecha != null) ? fecha : LocalDate.now();
        ventaService.actualizarVenta(idVentas, clienteDocumento, fechaVenta, estadoId, items);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/ventas";
    }

    // =====================================================
    // CAMBIO RÁPIDO DE ESTADO (AJAX)
    // =====================================================
    @PostMapping("/cambiar-estado")
    @ResponseBody
    public ResponseEntity<?> cambiarEstado(
            @RequestParam("idVenta") Integer idVenta,
            @RequestParam("estadoId") Integer estadoId) {
        try {
            ventaService.cambiarEstado(idVenta, estadoId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Estado de la venta actualizado exitosamente."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Error al actualizar estado."));
        }
    }

    // =====================================================
    // DETALLE DE VENTA (JSON PARA MODAL)
    // =====================================================
    @GetMapping("/api/detalle/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerDetalle(@PathVariable("id") Integer id) {
        VentaDetalleDTO detalle = ventaService.obtenerDetalleDTO(id);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalle);
    }

    // =====================================================
    // ELIMINAR VENTA
    // =====================================================
    @PostMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable("id") Integer id) {
        ventaService.eliminarVenta(id);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/ventas";
    }
}
