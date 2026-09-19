package com.apiv1.omniModa.Controllers.Account;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.apiv1.omniModa.Models.Entity.Usuarios;

@Controller
public class AccountPayloadController {

    @GetMapping({"/AccountIndex", "/login"})
    public String Index(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            String rol = (String) session.getAttribute("rolPrincipal");
            if ("CLIENTE".equalsIgnoreCase(rol)) {
                return "redirect:/cliente/inicio";
            }
            return "redirect:/dashboard";
        }
        return "home/login";
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            String rol = (String) session.getAttribute("rolPrincipal");
            if ("CLIENTE".equalsIgnoreCase(rol)) {
                return "redirect:/cliente/inicio";
            }
            return "redirect:/dashboard";
        }
        return "redirect:/AccountIndex";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/AccountIndex?logout=true";
    }
}
