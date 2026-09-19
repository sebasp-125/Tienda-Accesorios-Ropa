package com.apiv1.omniModa.Models.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Entity.Roles;
import com.apiv1.omniModa.Models.Entity.TipoCliente;
import com.apiv1.omniModa.Models.Entity.Usuario_rol;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Repository.ClienteRepository;
import com.apiv1.omniModa.Models.Repository.RolRepository;
import com.apiv1.omniModa.Models.Repository.TipoClienteRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRolRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final ClienteRepository clienteRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            UsuarioRolRepository usuarioRolRepository,
            ClienteRepository clienteRepository,
            TipoClienteRepository tipoClienteRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.clienteRepository = clienteRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuarios> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Roles> listarRoles() {
        return rolRepository.findAll();
    }

    public Optional<Usuarios> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuarios> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public List<Usuario_rol> obtenerRolesUsuario(Usuarios usuario) {
        return usuarioRolRepository.findByUsuario(usuario);
    }

    public String obtenerRolPrincipal(Usuarios usuario) {
        List<Usuario_rol> asignaciones = usuarioRolRepository.findByUsuario(usuario);
        if (asignaciones.isEmpty()) {
            return "CLIENTE";
        }
        for (Usuario_rol ur : asignaciones) {
            if ("ADMINISTRADOR".equalsIgnoreCase(ur.getRol().getTipo())) {
                return "ADMINISTRADOR";
            }
        }
        for (Usuario_rol ur : asignaciones) {
            if ("VENTAS".equalsIgnoreCase(ur.getRol().getTipo())) {
                return "VENTAS";
            }
        }
        return asignaciones.get(0).getRol().getTipo().toUpperCase();
    }

    public Roles obtenerRolEntidadPrincipal(Usuarios usuario) {
        List<Usuario_rol> asignaciones = usuarioRolRepository.findByUsuario(usuario);
        if (asignaciones.isEmpty()) {
            return null;
        }
        for (Usuario_rol ur : asignaciones) {
            if ("ADMINISTRADOR".equalsIgnoreCase(ur.getRol().getTipo())) {
                return ur.getRol();
            }
        }
        for (Usuario_rol ur : asignaciones) {
            if ("VENTAS".equalsIgnoreCase(ur.getRol().getTipo())) {
                return ur.getRol();
            }
        }
        return asignaciones.get(0).getRol();
    }

    @Transactional
    public Usuarios autenticar(String correo, String passwordPlana) {
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            return null;
        }

        if (!passwordEncoder.matches(passwordPlana, usuario.getPassword())) {
            return null;
        }

        return usuario;
    }

    @Transactional
    public Usuarios registrarCliente(String nombre, String correo, String passwordPlana) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo electrónico ya se encuentra registrado.");
        }

        Usuarios usuario = new Usuarios();
        usuario.setNombreUsuario(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(passwordEncoder.encode(passwordPlana));
        usuario.setEstado("ACTIVO");

        usuario = usuarioRepository.save(usuario);

        Roles rolCliente = rolRepository.findByTipoIgnoreCase("CLIENTE")
                .orElseGet(() -> rolRepository.save(new Roles(null, "CLIENTE")));

        Usuario_rol usuarioRol = new Usuario_rol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolCliente);
        usuarioRolRepository.save(usuarioRol);

        String docDefault = "C-" + System.currentTimeMillis();
        if (docDefault.length() > 20) {
            docDefault = docDefault.substring(0, 20);
        }

        TipoCliente tipoDefault = tipoClienteRepository.findAll().stream().findFirst().orElse(null);
        if (tipoDefault == null) {
            tipoDefault = tipoClienteRepository.save(new TipoCliente("Natural"));
        }

        Clientes nuevoCliente = new Clientes(
                docDefault,
                nombre,
                correo,
                "Sin registrar",
                tipoDefault
        );
        clienteRepository.save(nuevoCliente);

        return usuario;
    }

    @Transactional
    public Usuarios crearUsuarioTienda(String nombre, String correo, String passwordPlana, Integer rolId, String estado) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo electrónico ya se encuentra registrado.");
        }

        Usuarios usuario = new Usuarios();
        usuario.setNombreUsuario(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(passwordEncoder.encode(passwordPlana));
        usuario.setEstado(estado != null && !estado.isBlank() ? estado.toUpperCase() : "ACTIVO");

        usuario = usuarioRepository.save(usuario);

        Roles rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new IllegalArgumentException("El rol seleccionado no es válido."));

        Usuario_rol usuarioRol = new Usuario_rol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);
        usuarioRolRepository.save(usuarioRol);

        return usuario;
    }

    @Transactional
    public Usuarios actualizarUsuario(Integer id, String nombre, String correo, String nuevaPasswordPlana, Integer rolId, String estado) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        Optional<Usuarios> existenteConCorreo = usuarioRepository.findByCorreo(correo);
        if (existenteConCorreo.isPresent() && !existenteConCorreo.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
        }

        usuario.setNombreUsuario(nombre);
        usuario.setCorreo(correo);
        if (estado != null && !estado.isBlank()) {
            usuario.setEstado(estado.toUpperCase());
        }

        if (nuevaPasswordPlana != null && !nuevaPasswordPlana.isBlank()) {
            usuario.setPassword(passwordEncoder.encode(nuevaPasswordPlana));
        }

        usuario = usuarioRepository.save(usuario);

        if (rolId != null) {
            Roles nuevoRol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado."));

            List<Usuario_rol> asignaciones = usuarioRolRepository.findByUsuario(usuario);
            if (!asignaciones.isEmpty()) {
                Usuario_rol ur = asignaciones.get(0);
                ur.setRol(nuevoRol);
                usuarioRolRepository.save(ur);
                for (int i = 1; i < asignaciones.size(); i++) {
                    usuarioRolRepository.delete(asignaciones.get(i));
                }
            } else {
                Usuario_rol ur = new Usuario_rol();
                ur.setUsuario(usuario);
                ur.setRol(nuevoRol);
                usuarioRolRepository.save(ur);
            }
        }

        return usuario;
    }

    @Transactional
    public void cambiarEstado(Integer id) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if ("ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            usuario.setEstado("INACTIVO");
        } else {
            usuario.setEstado("ACTIVO");
        }
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        Usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioRolRepository.deleteByUsuario(usuario);
        usuarioRepository.delete(usuario);
    }
}
