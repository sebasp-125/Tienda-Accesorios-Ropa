package com.apiv1.omniModa.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.apiv1.omniModa.Models.Entity.Usuarios;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/")
                || path.startsWith("/h2-console") || path.equals("/favicon.ico")) {
            return true;
        }

        if (path.equals("/AccountIndex") || path.equals("/login") || path.startsWith("/api/auth/") || path.equals("/logout")) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("usuarioLogueado") != null) {
                if (path.equals("/AccountIndex") || path.equals("/login")) {
                    String rol = (String) session.getAttribute("rolPrincipal");
                    if ("CLIENTE".equalsIgnoreCase(rol)) {
                        response.sendRedirect(request.getContextPath() + "/cliente/inicio");
                        return false;
                    } else {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return false;
                    }
                }
            }
            return true;
        }

        HttpSession session = request.getSession(false);
        Usuarios usuario = (session != null) ? (Usuarios) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            String acceptHeader = request.getHeader("Accept");
            String requestedWith = request.getHeader("X-Requested-With");
            if (path.startsWith("/api/") || (acceptHeader != null && acceptHeader.contains("application/json"))
                    || "XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"No autenticado. Por favor inicie sesión.\",\"redirectUrl\":\"/AccountIndex\"}");
            } else {
                response.sendRedirect(request.getContextPath() + "/AccountIndex");
            }
            return false;
        }

        String rolPrincipal = (String) session.getAttribute("rolPrincipal");
        if (rolPrincipal == null) {
            rolPrincipal = "CLIENTE";
        }

        if ("CLIENTE".equalsIgnoreCase(rolPrincipal)) {
            if (path.startsWith("/cliente/")) {
                return true;
            }

            if (path.startsWith("/dashboard") || path.startsWith("/clientes") || path.startsWith("/productos")
                    || path.startsWith("/proveedores") || path.startsWith("/promociones")
                    || path.startsWith("/ventas") || path.startsWith("/usuarios") || path.startsWith("/roles")) {
                response.sendRedirect(request.getContextPath() + "/cliente/inicio?acceso_denegado=true");
                return false;
            }

            if (path.equals("/") || path.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cliente/inicio");
                return false;
            }
        }

        if ("VENTAS".equalsIgnoreCase(rolPrincipal)) {
            if (path.startsWith("/usuarios") || path.startsWith("/roles")) {
                response.sendRedirect(request.getContextPath() + "/dashboard?acceso_denegado=true");
                return false;
            }
        }

        if (path.equals("/") || path.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }

        return true;
    }
}
