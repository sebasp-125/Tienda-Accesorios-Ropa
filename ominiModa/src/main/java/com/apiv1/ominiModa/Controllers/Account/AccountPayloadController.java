package com.apiv1.ominiModa.Controllers.Account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class AccountPayloadController {

    @GetMapping("/AccountIndex")
    public String Index(){
        return "login";
    }
}
