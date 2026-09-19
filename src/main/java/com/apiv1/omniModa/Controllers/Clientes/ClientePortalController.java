package com.apiv1.omniModa.Controllers.Clientes;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Service.ProductoService;
import com.apiv1.omniModa.Models.Service.PromocionesService;

@Controller
public class ClientePortalController {

    private final ProductoService productoService;
    private final PromocionesService promocionesService;

    public ClientePortalController(ProductoService productoService, PromocionesService promocionesService) {
        this.productoService = productoService;
        this.promocionesService = promocionesService;
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
        model.addAttribute("accesoDenegado", Boolean.TRUE.equals(accesoDenegado));

        return "client_portal/inicio";
    }
}
