package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/formlogin")
    public String login() {
        return "auth/login";
    }
    
}
