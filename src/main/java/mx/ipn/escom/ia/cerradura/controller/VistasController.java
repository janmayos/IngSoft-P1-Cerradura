package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller

public class VistasController {

    @GetMapping("/formlogin")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/formregister")
    public String registro() {
        return "auth/Registro";
    }


    @GetMapping("/PaginaInicio")
    public String paginaInicio() {
        
        return "auth/PaginaInicio";
    }

    @GetMapping("/")
	public String home() {
		return "redirect:/formlogin";
	}

    @GetMapping("/resultadosLibros")
    public String resultadosLibros() {
        return "libros/resultados";
    }
    
}
