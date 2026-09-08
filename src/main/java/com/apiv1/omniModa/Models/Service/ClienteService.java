package com.apiv1.omniModa.Models.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository ClienteRepository;

    public ClienteService(ClienteRepository ClienteRepository) {
        this.ClienteRepository = ClienteRepository;
    }

    public List<Clientes> listarClientes() {
        return ClienteRepository.findAll();
    }

    public Clientes guardarCliente(Clientes cliente) {
        return ClienteRepository.save(cliente);
    }

    public Clientes buscarPorDocumento(String documento) {
        return ClienteRepository.findById(documento).orElse(null);
    }

    public void eliminarCliente(String documento) {
        ClienteRepository.deleteById(documento);
    }
}