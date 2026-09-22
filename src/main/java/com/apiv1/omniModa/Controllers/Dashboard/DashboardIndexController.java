package com.apiv1.omniModa.Controllers.Dashboard;

import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiv1.omniModa.Models.DTO.ReporteDatosDTO;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Repository.ClienteRepository;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRepository;
import com.apiv1.omniModa.Models.Service.ProductoService;
import com.apiv1.omniModa.Models.Service.ReportePdfService;
import com.apiv1.omniModa.Models.Service.VentaService;

@Controller
public class DashboardIndexController {

        private final ClienteRepository clienteRepository;
        private final ProductoRepository productoRepository;
        private final ProveedorRepository proveedorRepository;
        private final UsuarioRepository usuarioRepository;
        private final VentaService ventaService;
        private final ProductoService productoService;
        private final ReportePdfService reportePdfService;

        public DashboardIndexController(
                        ClienteRepository clienteRepository,
                        ProductoRepository productoRepository,
                        ProveedorRepository proveedorRepository,
                        UsuarioRepository usuarioRepository,
                        VentaService ventaService,
                        ProductoService productoService,
                        ReportePdfService reportePdfService) {
                this.clienteRepository = clienteRepository;
                this.productoRepository = productoRepository;
                this.proveedorRepository = proveedorRepository;
                this.usuarioRepository = usuarioRepository;
                this.ventaService = ventaService;
                this.productoService = productoService;
                this.reportePdfService = reportePdfService;
        }

        @GetMapping("/dashboard")
        public String Dashboard(
                        @RequestParam(value = "acceso_denegado", required = false) Boolean accesoDenegado,
                        Model model) {

                long stockBajo = productoRepository.findAll().stream()
                                .filter(p -> p.getStockDisponible() != null && p.getStockDisponible() <= 5)
                                .count();

                model.addAttribute("totalClientes", clienteRepository.count());
                model.addAttribute("totalProductos", productoRepository.count());
                model.addAttribute("totalProveedores", proveedorRepository.count());
                model.addAttribute("totalUsuarios", usuarioRepository.count());
                model.addAttribute("totalVentas", ventaService.contarTotalVentas());
                model.addAttribute("productosStockBajo", stockBajo);
                model.addAttribute("ventasRecientes", ventaService.obtenerVentasRecientes(5));
                model.addAttribute("paginaActual", "dashboard");
                model.addAttribute("accesoDenegado", Boolean.TRUE.equals(accesoDenegado));

                model.addAttribute(
                                "productosStockBajo",
                                productoService.contarProductosStockBajo());

                model.addAttribute(
                                "productosBajoStock",
                                productoService.listarProductosStockBajoDashboard());

                return "home/dashboard";
        }

        /**
         * Endpoint protegido exclusivo para ADMINISTRADOR que genera el reporte en PDF
         * con formato sobrio, profesional en blanco y negro y estructurado por
         * conceptos.
         */
        @GetMapping("/dashboard/reportes/pdf")
        public ResponseEntity<?> generarReportePdf(
                        @RequestParam(value = "tipo", defaultValue = "general") String tipo,
                        HttpSession session) {

                Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
                boolean esAdmin = Boolean.TRUE.equals(session.getAttribute("esAdmin"))
                                || "ADMINISTRADOR".equalsIgnoreCase((String) session.getAttribute("rolPrincipal"));

                if (usuario == null || !esAdmin) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                ReporteDatosDTO datos = new ReporteDatosDTO();
                datos.setAdminNombre(usuario.getNombreUsuario());
                datos.setTipoReporte(tipo);
                datos.setTotalClientes(clienteRepository.count());
                datos.setTotalProductos(productoRepository.count());
                datos.setTotalProveedores(proveedorRepository.count());
                datos.setTotalUsuarios(usuarioRepository.count());
                datos.setTotalVentas(ventaService.contarTotalVentas());
                datos.setTotalIngresos(ventaService.calcularTotalIngresos());
                datos.setVentasPagadas(ventaService.contarPorEstado("PAGADA"));
                datos.setVentasPendientes(ventaService.contarPorEstado("PENDIENTE"));
                datos.setVentasCanceladas(ventaService.contarPorEstado("CANCELADA"));
                datos.setProductosBajoStock(productoRepository.findAll().stream()
                                .filter(p -> p.getStockDisponible() != null && p.getStockDisponible() <= 5)
                                .toList());
                datos.setTodosProductos(productoRepository.findAll());
                datos.setVentasRecientes(ventaService.obtenerVentasRecientes(10));

                byte[] pdfBytes = reportePdfService.generarReportePdf(datos);
                String filename = String.format("Reporte_%s_%s.pdf", tipo, LocalDate.now());

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(pdfBytes);
        }
}
