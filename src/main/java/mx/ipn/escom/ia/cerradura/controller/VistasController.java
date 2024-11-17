package mx.ipn.escom.ia.cerradura.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VistasController {

    private final UsuarioService usuarioService;

    @GetMapping("/formlogin")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/formregister")
    public String registro() {
        return "auth/Registro";
    }

    @GetMapping("/PaginaInicio")
    public String paginaInicio(@RequestParam("id") Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toList()));
        return "auth/PaginaInicio";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/formlogin";
    }

    @GetMapping("/resultadosLibros")
    public String libros(@RequestParam("id") Long id, Model model) {
        model.addAttribute("currentUserId", id);
        return "libros/resultados";
    }
}
