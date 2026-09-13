package com.apiv1.omniModa.Models.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiv1.omniModa.Models.Entity.Categorias;

public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {

}
