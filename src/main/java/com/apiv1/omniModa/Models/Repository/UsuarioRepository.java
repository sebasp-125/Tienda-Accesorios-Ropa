package com.apiv1.omniModa.Models.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apiv1.omniModa.Models.Entity.Usuarios;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
