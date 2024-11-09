package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.ui.Model;


@Controller

public class LoginController {

    @GetMapping("/formlogin")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/formregister")
    public String registro() {
        return "auth/Registro";
    }


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/PaginaInicio")
    public String paginaInicio() {
        return "auth/PaginaInicio";
    }

    
}
