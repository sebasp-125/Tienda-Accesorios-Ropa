package com.apiv1.omniModa.Controllers.Clientes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apiv1.omniModa.Models.DTO.CompraRequestDTO;
import com.apiv1.omniModa.Models.DTO.VentaDetalleDTO;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Service.ProductoService;
import com.apiv1.omniModa.Models.Service.PromocionesService;
import com.apiv1.omniModa.Models.Service.VentaService;

@Controller
public class ClientePortalController {

    private final ProductoService productoService;
    private final PromocionesService promocionesService;
    private final VentaService ventaService;

    public ClientePortalController(
            ProductoService productoService,
            PromocionesService promocionesService,
            VentaService ventaService) {
        this.productoService = productoService;
        this.promocionesService = promocionesService;
        this.ventaService = ventaService;
    }

    @GetMapping("/cliente/inicio")
    public String inicioCliente(
            @RequestParam(value = "acceso_denegado", required = false) Boolean accesoDenegado,
            HttpSession session,
            Model model) {

        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("promociones", promocionesService.listarPromociones());
        model.addAttribute("compras", ventaService.obtenerComprasCliente(usuario));
        model.addAttribute("accesoDenegado", Boolean.TRUE.equals(accesoDenegado));

        return "client_portal/inicio";
    }

    @PostMapping("/cliente/api/comprar")
    @ResponseBody
    public ResponseEntity<?> comprar(
            @RequestBody CompraRequestDTO request,
            HttpSession session) {

        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Debes iniciar sesión para realizar compras."));
        }

        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "El carrito está vacío. Agrega productos antes de comprar."));
        }

        try {
            Ventas venta = ventaService.procesarCompraCliente(usuario, request.getItems());
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("idVenta", venta.getIdVentas());
            resp.put("total", venta.getTotal());
            resp.put("message", "¡Compra exitosa! Se ha registrado tu pedido con ID #" + venta.getIdVentas());
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al procesar la compra: " + e.getMessage()));
        }
    }

    @GetMapping("/cliente/api/mis-compras")
    @ResponseBody
    public ResponseEntity<?> misCompras(HttpSession session) {
        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Ventas> compras = ventaService.obtenerComprasCliente(usuario);
        List<Map<String, Object>> lista = compras.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idVentas", v.getIdVentas());
            map.put("fecha", v.getFecha().toString());
            map.put("total", v.getTotal());
            map.put("estado", v.getEstado() != null ? v.getEstado().getTipo() : "PENDIENTE");
            map.put("cantidadItems", v.getDetalles() != null ? v.getDetalles().size() : 0);
            return map;
        }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/cliente/api/compras/{id}")
    @ResponseBody
    public ResponseEntity<?> detalleCompra(@PathVariable Integer id, HttpSession session) {
        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        VentaDetalleDTO detalle = ventaService.obtenerDetalleDTO(id);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }

        // Validar que la compra pertenece al cliente conectado
        if (!detalle.getClienteCorreo().equalsIgnoreCase(usuario.getCorreo())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "No tienes permisos para ver este pedido."));
        }

        return ResponseEntity.ok(detalle);
    }
}
