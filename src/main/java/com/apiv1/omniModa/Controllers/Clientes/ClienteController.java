package com.apiv1.omniModa.Controllers.Clientes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Repository.TipoClienteRepository;
import com.apiv1.omniModa.Models.Service.ClienteService;

@Controller
public class ClienteController {

    private final ClienteService clienteService;
    private final TipoClienteRepository tipoClienteRepository;

    public ClienteController(
            ClienteService clienteService,
            TipoClienteRepository tipoClienteRepository) {

        this.clienteService = clienteService;
        this.tipoClienteRepository = tipoClienteRepository;
    }

    // LISTAR CLIENTES
    @GetMapping("/clientes")
    public String clientesIndex(Model model) {

        model.addAttribute("clientes", clienteService.listarClientes());

        return "client/clientes";
    }

    // MOSTRAR FORMULARIO NUEVO CLIENTE
    @GetMapping("/clientes/nuevo")
    public String nuevoCliente(Model model) {

        model.addAttribute("cliente", new Clientes());

        model.addAttribute(
                "tiposCliente",
                tipoClienteRepository.findAll());

        return "client/nuevo_cliente";
    }

    // GUARDAR CLIENTE
    @PostMapping("/clientes/guardar")
    public String guardarCliente(Clientes cliente) {

        clienteService.guardarCliente(cliente);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/clientes";
    }

    // MOSTRAR FORMULARIO ACTUALIZAR CLIENTE
    @GetMapping("/clientes/actualizar/{documento}")
    public String actualizarCliente(@PathVariable String documento, Model model) {
        Clientes cliente = clienteService.buscarPorDocumento(documento);
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("tiposCliente", tipoClienteRepository.findAll());
        return "client/actualizar_cliente";
    }

    // ACTUALIZAR CLIENTE
    @PostMapping("/clientes/actualizar")
    public String guardarActualizacion(Clientes cliente) {
        clienteService.guardarCliente(cliente);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/clientes";
    }

    // ELIMINAR CLIENTE

    @PostMapping("/clientes/eliminar/{documento}")
    public String eliminarCliente(
            @PathVariable String documento) {
        clienteService.eliminarCliente(documento);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/clientes";
    }
}