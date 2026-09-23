package com.apiv1.omniModa.Controllers.Promociones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apiv1.omniModa.Models.Entity.Categorias;
import com.apiv1.omniModa.Models.Entity.Promociones;
import com.apiv1.omniModa.Models.Entity.TipoCliente;
import com.apiv1.omniModa.Models.Repository.CategoriaRepository;
import com.apiv1.omniModa.Models.Repository.TipoClienteRepository;
import com.apiv1.omniModa.Models.Service.PromocionesService;

@Controller
@RequestMapping("/promociones")
public class PromocionesController {

    @Autowired
    private PromocionesService promocionesService;

    @Autowired
    private TipoClienteRepository tipoClienteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "promociones",
                promocionesService.listarPromociones());

        return "promotion/promociones";
    }

    @GetMapping("/nueva")
    public String nuevaPromocion(Model model) {

        model.addAttribute(
                "promocion",
                new Promociones());

        model.addAttribute(
                "tiposCliente",
                tipoClienteRepository.findAll());

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll());

        return "promotion/nueva_promocion";
    }

    @PostMapping("/guardar")
    public String guardar(
            Promociones promocion,
            @RequestParam("tipoClienteId") Integer tipoClienteId,
            @RequestParam("categoriaId") Integer categoriaId) {

        TipoCliente tipoCliente = tipoClienteRepository.findById(tipoClienteId)
                .orElse(null);

        Categorias categoria = categoriaRepository.findById(categoriaId)
                .orElse(null);

        promocion.setTipoCliente(tipoCliente);
        promocion.setCategoria(categoria);

        promocionesService.guardar(promocion);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/promociones";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable("id") Integer id,
            Model model) {

        Promociones promocion = promocionesService.buscarPorId(id);

        if (promocion == null) {
            return "redirect:/promociones";
        }

        model.addAttribute(
                "promocion",
                promocion);

        model.addAttribute(
                "tiposCliente",
                tipoClienteRepository.findAll());

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll());

        return "promotion/actualizar_promocion";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            Promociones promocion,
            @RequestParam("tipoClienteId") Integer tipoClienteId,
            @RequestParam("categoriaId") Integer categoriaId) {

        TipoCliente tipoCliente = tipoClienteRepository.findById(tipoClienteId)
                .orElse(null);

        Categorias categoria = categoriaRepository.findById(categoriaId)
                .orElse(null);

        promocion.setTipoCliente(tipoCliente);
        promocion.setCategoria(categoria);

        promocionesService.guardar(promocion);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/promociones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable("id") Integer id) {

        promocionesService.eliminar(id);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/promociones";
    }
}