package com.apiv1.ominiModa.Controllers.Dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardIndexController {

    @GetMapping("/dashboard")
    public String DashboardIndex(){
        return "dashboard";
    }
}
