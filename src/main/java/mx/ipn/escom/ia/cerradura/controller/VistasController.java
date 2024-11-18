package mx.ipn.escom.ia.cerradura.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.HttpStatus;

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
    public String paginaInicio(@RequestParam(name = "id", required = false, defaultValue = "0") Long id, Model model) {
        if (id == 0) {
            return "redirect:/formlogin";
        }

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
    public String resultadosLibros() {
        return "resultadosLibros";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return "redirect:/formlogin";
    }
}
