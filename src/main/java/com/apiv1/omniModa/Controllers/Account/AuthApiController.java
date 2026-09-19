package com.apiv1.omniModa.Controllers.Account;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final UsuarioService usuarioService;

    public AuthApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.error("Debes ingresar correo y contraseña."));
        }

        Usuarios usuario = usuarioService.autenticar(request.getEmail().trim(), request.getPassword());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error("Credenciales incorrectas o usuario inactivo."));
        }

        String rolPrincipal = usuarioService.obtenerRolPrincipal(usuario);
        String redirectUrl = "CLIENTE".equalsIgnoreCase(rolPrincipal) ? "/cliente/inicio" : "/dashboard";

        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rolPrincipal", rolPrincipal);
        session.setAttribute("esAdmin", "ADMINISTRADOR".equalsIgnoreCase(rolPrincipal));
        session.setAttribute("esVentas", "VENTAS".equalsIgnoreCase(rolPrincipal));
        session.setAttribute("esCliente", "CLIENTE".equalsIgnoreCase(rolPrincipal));

        return ResponseEntity.ok(AuthResponse.ok("Inicio de sesión exitoso", redirectUrl, usuario.getNombreUsuario(), rolPrincipal));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpSession session) {
        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.badRequest().body(AuthResponse.error("El nombre completo es obligatorio."));
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body(AuthResponse.error("Ingresa un correo electrónico válido."));
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(AuthResponse.error("La contraseña debe tener al menos 6 caracteres."));
        }

        if (usuarioService.existeCorreo(request.getEmail().trim())) {
            return ResponseEntity.badRequest().body(AuthResponse.error("El correo electrónico ya se encuentra registrado."));
        }

        try {
            Usuarios nuevoUsuario = usuarioService.registrarCliente(
                    request.getName().trim(),
                    request.getEmail().trim(),
                    request.getPassword()
            );

            session.setAttribute("usuarioLogueado", nuevoUsuario);
            session.setAttribute("rolPrincipal", "CLIENTE");
            session.setAttribute("esAdmin", false);
            session.setAttribute("esVentas", false);
            session.setAttribute("esCliente", true);

            return ResponseEntity.ok(AuthResponse.ok("Cuenta creada exitosamente.", "/cliente/inicio", nuevoUsuario.getNombreUsuario(), "CLIENTE"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.error("Error al registrar cuenta: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(AuthResponse.ok("Sesión cerrada.", "/AccountIndex", null, null));
    }
}
