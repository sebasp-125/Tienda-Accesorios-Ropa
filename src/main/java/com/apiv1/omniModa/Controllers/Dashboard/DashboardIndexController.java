package com.apiv1.omniModa.Controllers.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardIndexController {

    @GetMapping("/dashboard")
    public String Dashboard() {
        return "home/dashboard";
    }
}
