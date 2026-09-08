package com.apiv1.omniModa.Models.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiv1.omniModa.Models.Entity.TipoCliente;

public interface TipoClienteRepository extends JpaRepository<TipoCliente, Integer> {

}
