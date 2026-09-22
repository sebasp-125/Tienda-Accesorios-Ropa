package com.apiv1.omniModa.Controllers.Clientes;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.apiv1.omniModa.Models.DTO.ItemVentaDTO;
import com.apiv1.omniModa.Models.DTO.VentaDetalleDTO;
import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Service.EmailServicio;
import com.apiv1.omniModa.Models.Service.FacturaPdfService;
import com.apiv1.omniModa.Models.Service.ProductoService;
import com.apiv1.omniModa.Models.Service.PromocionesService;
import com.apiv1.omniModa.Models.Service.VentaService;

@Controller
public class ClientePortalController {

    private final ProductoService productoService;
    private final PromocionesService promocionesService;
    private final VentaService ventaService;
    private final FacturaPdfService facturaPdfService;
    private final EmailServicio emailServicio;

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public ClientePortalController(
            ProductoService productoService,
            PromocionesService promocionesService,
            VentaService ventaService,
            FacturaPdfService facturaPdfService,
            EmailServicio emailServicio) {
        this.productoService = productoService;
        this.promocionesService = promocionesService;
        this.ventaService = ventaService;
        this.facturaPdfService = facturaPdfService;
        this.emailServicio = emailServicio;
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

        String metodoPago = (request.getMetodoPago() != null && !request.getMetodoPago().isBlank())
                ? request.getMetodoPago().trim()
                : "Tarjeta / Pago en Línea";

        try {
            Ventas venta = ventaService.procesarCompraCliente(usuario, request.getItems());
            String numFactura = String.format("FAC-%06d", venta.getIdVentas());
            String codAutorizacion = "AUT-" + (748291 + venta.getIdVentas() * 41);
            String fechaStr = venta.getFecha() != null ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

            double totalFinal = venta.getTotal() != null ? venta.getTotal() : 0.0;
            double baseImponible = totalFinal / 1.19;
            double iva19 = totalFinal - baseImponible;

            // Construir lista de items formateados
            List<Map<String, Object>> itemsFormateados = new ArrayList<>();
            if (venta.getDetalles() != null) {
                for (Detalle_venta d : venta.getDetalles()) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("codigo", d.getProducto() != null ? d.getProducto().getCodigo() : "ART");
                    itemMap.put("nombre", d.getProducto() != null ? d.getProducto().getNombre() : "Prenda");
                    itemMap.put("talla", d.getProducto() != null ? d.getProducto().getTalla() : "Única");
                    itemMap.put("color", d.getProducto() != null ? d.getProducto().getColor() : "N/A");
                    itemMap.put("cantidad", d.getCantidad());
                    itemMap.put("precioUnitario", d.getPrecioUnitario());
                    itemMap.put("precioUnitarioFormatted", CURRENCY_FORMAT.format(d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0.0));
                    itemMap.put("subtotal", d.getSubtotal());
                    itemMap.put("subtotalFormatted", CURRENCY_FORMAT.format(d.getSubtotal() != null ? d.getSubtotal() : 0.0));
                    itemsFormateados.add(itemMap);
                }
            }

            // 1. Generar la factura en PDF
            byte[] pdfBytes = null;
            try {
                pdfBytes = facturaPdfService.generarFacturaPdf(venta, metodoPago);
            } catch (Exception ex) {
                System.err.println("Error al generar PDF de factura: " + ex.getMessage());
            }

            // 2. Enviar correo electrónico con la factura y el archivo PDF adjunto
            boolean correoEnviado = false;
            String correoDestino = usuario.getCorreo();
            if (correoDestino != null && !correoDestino.isBlank()) {
                Map<String, Object> varsEmail = new HashMap<>();
                varsEmail.put("nombre", usuario.getNombreUsuario());
                varsEmail.put("numeroFactura", numFactura);
                varsEmail.put("fecha", fechaStr);
                varsEmail.put("metodoPago", metodoPago);
                varsEmail.put("codigoAutorizacion", codAutorizacion);
                varsEmail.put("subtotal", CURRENCY_FORMAT.format(baseImponible));
                varsEmail.put("iva", CURRENCY_FORMAT.format(iva19));
                varsEmail.put("total", CURRENCY_FORMAT.format(totalFinal));
                varsEmail.put("items", itemsFormateados);

                correoEnviado = emailServicio.enviarFacturaCliente(
                        correoDestino,
                        "Comprobante de Pago y Factura " + numFactura + " - OmniModa",
                        varsEmail,
                        pdfBytes,
                        numFactura + ".pdf"
                );
            }

            // 3. Respuesta JSON completa para renderizar la factura comercial en pantalla
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("idVenta", venta.getIdVentas());
            resp.put("numeroFactura", numFactura);
            resp.put("fecha", fechaStr);
            resp.put("metodoPago", metodoPago);
            resp.put("codigoAutorizacion", codAutorizacion);
            resp.put("estado", venta.getEstado() != null ? venta.getEstado().getTipo() : "PAGADA");
            resp.put("clienteNombre", venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : usuario.getNombreUsuario());
            resp.put("clienteDocumento", venta.getCliente() != null ? venta.getCliente().getDocumento() : "N/A");
            resp.put("clienteCorreo", correoDestino);
            resp.put("clienteTelefono", venta.getCliente() != null ? venta.getCliente().getTelefono() : "N/A");
            resp.put("subtotal", CURRENCY_FORMAT.format(baseImponible));
            resp.put("iva", CURRENCY_FORMAT.format(iva19));
            resp.put("total", totalFinal);
            resp.put("totalFormatted", CURRENCY_FORMAT.format(totalFinal));
            resp.put("items", itemsFormateados);
            resp.put("correoEnviado", correoEnviado);
            resp.put("correoDestino", correoDestino);
            resp.put("message", "¡Compra exitosa! Se ha generado tu Factura #" + numFactura + ".");

            return ResponseEntity.ok(resp);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al procesar la compra: " + e.getMessage()));
        }
    }

    @GetMapping("/cliente/api/factura/{id}/pdf")
    public ResponseEntity<?> descargarFacturaPdf(@PathVariable Integer id, HttpSession session) {
        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ventas venta = ventaService.buscarPorId(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        // Validar que la compra pertenece al cliente autenticado
        if (venta.getCliente() != null && venta.getCliente().getCorreo() != null) {
            if (!venta.getCliente().getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        byte[] pdf = facturaPdfService.generarFacturaPdf(venta, "Pago Electrónico Aprobado");
        String filename = String.format("Factura-FAC-%06d.pdf", id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/cliente/api/factura/{id}/datos")
    @ResponseBody
    public ResponseEntity<?> datosFactura(@PathVariable Integer id, HttpSession session) {
        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Ventas venta = ventaService.buscarPorId(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        if (venta.getCliente() != null && venta.getCliente().getCorreo() != null) {
            if (!venta.getCliente().getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "No tienes permisos para ver esta factura."));
            }
        }

        String numFactura = String.format("FAC-%06d", venta.getIdVentas());
        String codAutorizacion = "AUT-" + (748291 + venta.getIdVentas() * 41);
        String fechaStr = venta.getFecha() != null ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

        double totalFinal = venta.getTotal() != null ? venta.getTotal() : 0.0;
        double baseImponible = totalFinal / 1.19;
        double iva19 = totalFinal - baseImponible;

        List<Map<String, Object>> itemsFormateados = new ArrayList<>();
        if (venta.getDetalles() != null) {
            for (Detalle_venta d : venta.getDetalles()) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("codigo", d.getProducto() != null ? d.getProducto().getCodigo() : "ART");
                itemMap.put("nombre", d.getProducto() != null ? d.getProducto().getNombre() : "Prenda");
                itemMap.put("talla", d.getProducto() != null ? d.getProducto().getTalla() : "Única");
                itemMap.put("color", d.getProducto() != null ? d.getProducto().getColor() : "N/A");
                itemMap.put("cantidad", d.getCantidad());
                itemMap.put("precioUnitario", d.getPrecioUnitario());
                itemMap.put("precioUnitarioFormatted", CURRENCY_FORMAT.format(d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0.0));
                itemMap.put("subtotal", d.getSubtotal());
                itemMap.put("subtotalFormatted", CURRENCY_FORMAT.format(d.getSubtotal() != null ? d.getSubtotal() : 0.0));
                itemsFormateados.add(itemMap);
            }
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("idVenta", venta.getIdVentas());
        resp.put("numeroFactura", numFactura);
        resp.put("fecha", fechaStr);
        resp.put("metodoPago", "Pago Registrado / En Línea");
        resp.put("codigoAutorizacion", codAutorizacion);
        resp.put("estado", venta.getEstado() != null ? venta.getEstado().getTipo() : "PAGADA");
        resp.put("clienteNombre", venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : usuario.getNombreUsuario());
        resp.put("clienteDocumento", venta.getCliente() != null ? venta.getCliente().getDocumento() : "N/A");
        resp.put("clienteCorreo", venta.getCliente() != null ? venta.getCliente().getCorreo() : usuario.getCorreo());
        resp.put("clienteTelefono", venta.getCliente() != null ? venta.getCliente().getTelefono() : "N/A");
        resp.put("subtotal", CURRENCY_FORMAT.format(baseImponible));
        resp.put("iva", CURRENCY_FORMAT.format(iva19));
        resp.put("total", totalFinal);
        resp.put("totalFormatted", CURRENCY_FORMAT.format(totalFinal));
        resp.put("items", itemsFormateados);

        return ResponseEntity.ok(resp);
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
