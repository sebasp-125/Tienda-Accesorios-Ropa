package com.apiv1.omniModa.Controllers.Usuarios;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiv1.omniModa.Models.Entity.Roles;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Service.UsuarioService;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public static class UsuarioItem {
        private Integer idUsuario;
        private String nombreUsuario;
        private String correo;
        private String estado;
        private String rol;
        private Integer idRol;

        public UsuarioItem(Integer idUsuario, String nombreUsuario, String correo, String estado, String rol, Integer idRol) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.correo = correo;
            this.estado = estado;
            this.rol = rol;
            this.idRol = idRol;
        }

        public Integer getIdUsuario() { return idUsuario; }
        public String getNombreUsuario() { return nombreUsuario; }
        public String getCorreo() { return correo; }
        public String getEstado() { return estado; }
        public String getRol() { return rol; }
        public Integer getIdRol() { return idRol; }
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuarios> lista = usuarioService.listarUsuarios();
        List<UsuarioItem> items = new ArrayList<>();

        for (Usuarios u : lista) {
            String rol = usuarioService.obtenerRolPrincipal(u);
            Roles rolEntidad = usuarioService.obtenerRolEntidadPrincipal(u);
            Integer rolId = (rolEntidad != null) ? rolEntidad.getIdRol() : null;
            items.add(new UsuarioItem(
                    u.getIdUsuario(),
                    u.getNombreUsuario(),
                    u.getCorreo(),
                    u.getEstado(),
                    rol,
                    rolId
            ));
        }

        model.addAttribute("usuarios", items);
        model.addAttribute("paginaActual", "usuarios");
        return "user/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("roles", usuarioService.listarRoles());
        model.addAttribute("paginaActual", "usuarios");
        return "user/nuevo_usuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("correo") String correo,
            @RequestParam("password") String password,
            @RequestParam("rolId") Integer rolId,
            @RequestParam(value = "estado", defaultValue = "ACTIVO") String estado) {

        usuarioService.crearUsuarioTienda(nombreUsuario, correo, password, rolId, estado);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuarioForm(@PathVariable Integer id, Model model) {
        Usuarios usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }

        Roles rolActual = usuarioService.obtenerRolEntidadPrincipal(usuario);
        Integer rolActualId = (rolActual != null) ? rolActual.getIdRol() : null;

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", usuarioService.listarRoles());
        model.addAttribute("rolActualId", rolActualId);
        model.addAttribute("paginaActual", "usuarios");

        return "user/actualizar_usuario";
    }

    @PostMapping("/usuarios/actualizar")
    public String guardarActualizacion(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("correo") String correo,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("rolId") Integer rolId,
            @RequestParam("estado") String estado) {

        usuarioService.actualizarUsuario(idUsuario, nombreUsuario, correo, password, rolId, estado);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        usuarioService.cambiarEstado(id);
        return "redirect:/usuarios";
    }
}
