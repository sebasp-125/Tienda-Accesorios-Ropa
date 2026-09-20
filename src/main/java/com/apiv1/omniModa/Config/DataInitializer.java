package com.apiv1.omniModa.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.apiv1.omniModa.Models.Entity.Estados;
import com.apiv1.omniModa.Models.Entity.Roles;
import com.apiv1.omniModa.Models.Entity.Usuario_rol;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Repository.EstadoRepository;
import com.apiv1.omniModa.Models.Repository.RolRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRolRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final JdbcTemplate jdbcTemplate;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            JdbcTemplate jdbcTemplate,
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            UsuarioRolRepository usuarioRolRepository,
            EstadoRepository estadoRepository,
            PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de OmniModa...");

        try {
            jdbcTemplate.execute("ALTER TABLE USUARIOS ALTER COLUMN PASSWORD VARCHAR(255)");
            log.info("Columna PASSWORD asegurada a VARCHAR(255).");
        } catch (Exception e) {
            log.warn("No fue necesario o falló modificar columna PASSWORD: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE USUARIOS ALTER COLUMN CORREO VARCHAR(100)");
            log.info("Columna CORREO asegurada a VARCHAR(100).");
        } catch (Exception e) {
            log.warn("No fue necesario o falló modificar columna CORREO: {}", e.getMessage());
        }

        Roles rolAdmin = seedRol("ADMINISTRADOR");
        Roles rolVentas = seedRol("VENTAS");
        Roles rolCliente = seedRol("CLIENTE");

        seedEstado("PENDIENTE");
        seedEstado("PAGADA");
        seedEstado("CANCELADA");

        if (!usuarioRepository.existsByCorreo("admin@omnimoda.com")) {
            Usuarios admin = new Usuarios();
            admin.setNombreUsuario("Administrador");
            admin.setCorreo("admin@omnimoda.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEstado("ACTIVO");
            admin = usuarioRepository.save(admin);

            Usuario_rol ur = new Usuario_rol();
            ur.setUsuario(admin);
            ur.setRol(rolAdmin);
            usuarioRolRepository.save(ur);

            log.info("Usuario Administrador por defecto creado: admin@omnimoda.com / admin123");
        }

        if (!usuarioRepository.existsByCorreo("ventas@omnimoda.com")) {
            Usuarios vendedor = new Usuarios();
            vendedor.setNombreUsuario("Asesor de Ventas");
            vendedor.setCorreo("ventas@omnimoda.com");
            vendedor.setPassword(passwordEncoder.encode("ventas123"));
            vendedor.setEstado("ACTIVO");
            vendedor = usuarioRepository.save(vendedor);

            Usuario_rol ur = new Usuario_rol();
            ur.setUsuario(vendedor);
            ur.setRol(rolVentas);
            usuarioRolRepository.save(ur);

            log.info("Usuario de Ventas por defecto creado: ventas@omnimoda.com / ventas123");
        }

        log.info("DataInitializer completado satisfactoriamente.");
    }

    private Roles seedRol(String nombreRol) {
        return rolRepository.findByTipoIgnoreCase(nombreRol)
                .orElseGet(() -> {
                    Roles r = new Roles();
                    r.setTipo(nombreRol.toUpperCase());
                    log.info("Rol '{}' registrado en el sistema.", nombreRol);
                    return rolRepository.save(r);
                });
    }

    private Estados seedEstado(String nombreEstado) {
        return estadoRepository.findByTipoIgnoreCase(nombreEstado)
                .orElseGet(() -> {
                    Estados e = new Estados();
                    e.setTipo(nombreEstado.toUpperCase());
                    log.info("Estado de venta '{}' registrado en el sistema.", nombreEstado);
                    return estadoRepository.save(e);
                });
    }
}
