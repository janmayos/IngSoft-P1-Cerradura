package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.service.RolService;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/vista/usuarios")
public class UsuarioVistasController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // Endpoint para obtener todos los usuarios
    @GetMapping({"", "/"})
    public String getClients(Model model) {
        List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", listaUsuarios);
        return "Usuarios/tabla";
    }

    // Endpoint para mostrar la vista de edición de un usuario específico
    @GetMapping("/editar/{id}")
    public String obtenerUsuarioPorId(@PathVariable Long id, Model model) {
        Usuario usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        if (Objects.equals(usuarioOpt.getIdUsuario(), id)) {
            model.addAttribute("usuario", usuarioOpt);
            model.addAttribute("todosLosRoles", todosLosRoles);
            return "Usuarios/editar"; // Nombre de la plantilla Thymeleaf para la vista de edición
        } else {
            return "redirect:/vista/usuarios"; // Redirige a la lista de usuarios si el usuario no se encuentra
        }
    }

    // Endpoint para actualizar un usuario
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(
        @PathVariable Long id, 
        @Validated @ModelAttribute("usuario") Usuario usuarioActualizado, 
        BindingResult result, 
        @RequestParam(value = "roles", required = false) List<Long> rolesIds, 
        Model model) {
    
        if (result.hasErrors()) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            return "Usuarios/editar";  // Redirigir a la misma página si hay errores
        }
    
        Set<Rol> rolesSet = rolesIds != null ? rolesIds.stream().map(rolService::obtenerRolPorId).collect(Collectors.toSet()) : Set.of();
        usuarioActualizado.setRoles(rolesSet);
        usuarioService.actualizarUsuario(id, usuarioActualizado);
        return "redirect:/vista/usuarios";
    }
    
}
