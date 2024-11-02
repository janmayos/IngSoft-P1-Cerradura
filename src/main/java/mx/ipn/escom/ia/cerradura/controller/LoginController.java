package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;

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

    @GetMapping("/PaginaInicio")
    public String paginaInicio(@ModelAttribute("Usuario") UsuarioDTO usuario) {
        System.out.println(usuario);
        if (usuario != null) {
            System.out.println("Nombre del usuario en sesión: " + usuario.getNombre());
        } else {
            System.out.println("No hay usuario en sesión.");
        }
        //System.out.println("Nombre del usuario en sesión: " + usuario.getNombre());
        //model.mergeAttributes(usuario);
        //model.addAttribute("nombre",usuario.getNombre());
       // model.addAttribute("nombre",usuario.getNombre());
        return "auth/PaginaInicio";
    }

    
}
