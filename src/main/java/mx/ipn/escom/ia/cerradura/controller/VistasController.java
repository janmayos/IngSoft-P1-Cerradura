package mx.ipn.escom.ia.cerradura.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import mx.ipn.escom.ia.cerradura.service.RolService;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import mx.ipn.escom.ia.cerradura.jwt.JwtService;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.response.InicioRequest;
import mx.ipn.escom.ia.cerradura.response.TokenIDEditRequest;
import mx.ipn.escom.ia.cerradura.response.IdRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VistasController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final RolService rolService;

    @GetMapping("/formlogin")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/formregister")
    public String registro() {
        return "auth/Registro";
    }

    @GetMapping("/paginaDeInicio")
    public String paginaDeInicio() {
        return "auth/paginaDeInicio";
    }

    @GetMapping("/admin/usuarios")
    public String tablaAdmin() {
        return "Usuarios/tablaAdmin";
    }
    
    @PostMapping("/admin/usuarios/contenido")
    public String tablaContenido(@RequestBody InicioRequest request, Model model) {
        String token = request.getToken();
        Long userId = jwtService.getUserIdFromToken(token);

        // Verificar el token y cargar la información del usuario
        Usuario usuarioActual = usuarioService.obtenerUsuarioPorId(userId);
        List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios();

        if (usuarioActual != null) {
            model.addAttribute("usuarios", listaUsuarios);
            model.addAttribute("usuarioActual", usuarioActual);
            return "Usuarios/tabla";
        } else {
            return "redirect:/formlogin";
        }
    }

    @PostMapping("/admin/usuarios/editarTablaPublica")
    public String editarTablaPublica(@RequestBody IdRequest request, @RequestHeader("Authorization") String token, Model model) {
        Long id = request.getIdModificar();
        Long userId = jwtService.getUserIdFromToken(token.replace("Bearer ", ""));

        // Verificar el token y cargar la información del usuario
        Usuario usuarioActual = usuarioService.obtenerUsuarioPorId(userId);
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();

        if (usuarioActual != null && usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("todosLosRoles", todosLosRoles);
            return "Usuarios/editarTabla";
        } else {
            return "redirect:/formlogin";
        }
    }

    @GetMapping("/admin/usuarios/editarTablaPublica")
    public String editarTablaPublica() {
        return "Usuarios/editarTablaPublica";
    }

    @PostMapping("/PaginaInicioContenido")
    public String paginaInicioContenido(@RequestBody InicioRequest request, Model model) {
        String token = request.getToken();
        Long userId = jwtService.getUserIdFromToken(token);

        // Verificar el token y cargar la información del usuario
        Usuario usuario = usuarioService.obtenerUsuarioPorId(userId);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toList()));
            return "auth/PaginaInicioContenido";
        } else {
            return "redirect:/formlogin";
        }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/formlogin";
    }

    @GetMapping("/resultadosLibros")
    public String libros(@RequestParam(name = "id", required = false, defaultValue = "0") Long id, Model model) {
        if (id == 0) {
            return "redirect:/formlogin";
        }

        model.addAttribute("currentUserId", id);
        return "libros/resultados";
    }
}