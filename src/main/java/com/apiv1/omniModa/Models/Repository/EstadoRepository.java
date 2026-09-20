package com.apiv1.omniModa.Models.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apiv1.omniModa.Models.Entity.Estados;

@Repository
public interface EstadoRepository extends JpaRepository<Estados, Integer> {

    Optional<Estados> findByTipoIgnoreCase(String tipo);

    boolean existsByTipoIgnoreCase(String tipo);
}
