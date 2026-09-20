package com.apiv1.omniModa.Controllers.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiv1.omniModa.Models.Repository.ClienteRepository;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.ProveedorRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRepository;
import com.apiv1.omniModa.Models.Service.VentaService;

@Controller
public class DashboardIndexController {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaService ventaService;

    public DashboardIndexController(
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository,
            ProveedorRepository proveedorRepository,
            UsuarioRepository usuarioRepository,
            VentaService ventaService) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaService = ventaService;
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

        return "home/dashboard";
    }
}
