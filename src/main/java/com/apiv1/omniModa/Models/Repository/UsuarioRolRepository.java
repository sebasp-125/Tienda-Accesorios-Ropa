package com.apiv1.omniModa.Models.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apiv1.omniModa.Models.Entity.Roles;
import com.apiv1.omniModa.Models.Entity.Usuario_rol;
import com.apiv1.omniModa.Models.Entity.Usuarios;

public interface UsuarioRolRepository extends JpaRepository<Usuario_rol, Integer> {
    List<Usuario_rol> findByUsuario(Usuarios usuario);
    List<Usuario_rol> findByUsuario_IdUsuario(Integer idUsuario);
    Optional<Usuario_rol> findByUsuarioAndRol(Usuarios usuario, Roles rol);
    void deleteByUsuario(Usuarios usuario);
}
