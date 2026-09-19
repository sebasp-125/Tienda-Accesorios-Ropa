package com.apiv1.omniModa.Models.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apiv1.omniModa.Models.Entity.Roles;

public interface RolRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByTipoIgnoreCase(String tipo);
    boolean existsByTipoIgnoreCase(String tipo);
}
