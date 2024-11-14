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

    // Endpoint para mostrar la vista de edición de un usuario específico desde la tabla
    @GetMapping("/editar/{id}")
    public String obtenerUsuarioPorIdDesdeTabla(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosRoles", todosLosRoles);
        return "Usuarios/editarTabla";
    }

    // Endpoint para mostrar la vista de edición de un usuario específico desde la página de inicio
    @GetMapping("/editarInicio/{id}")
    public String obtenerUsuarioPorIdDesdeInicio(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosRoles", todosLosRoles);
        return "Usuarios/editarInicio";
    }

    // Endpoint para actualizar un usuario
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(
        @PathVariable Long id, 
        @Validated @ModelAttribute("usuario") Usuario usuarioActualizado, 
        BindingResult result, 
        @RequestParam(value = "roles", required = false) List<Long> rolesIds, 
        @RequestParam(value = "redirect", required = false) String redirect,
        Model model) {
        
        Set<Rol> rolesSet = rolesIds != null ? rolesIds.stream().map(rolService::obtenerRolPorId).collect(Collectors.toSet()) : Set.of();
        usuarioActualizado.setRoles(rolesSet);

        if (rolesSet.isEmpty()) {
            result.rejectValue("roles", "error.usuario", "Debe seleccionar al menos un rol.");
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("error", "Debe seleccionar al menos un rol.");
            return "Usuarios/editar";
        }

        if (result.hasErrors()) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            return "Usuarios/editar";
        }

        try {
            usuarioService.actualizarUsuario(id, usuarioActualizado);
        } catch (IllegalArgumentException e) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("error", e.getMessage());
            return "Usuarios/editar";
        }

        if ("inicio".equals(redirect)) {
            return "redirect:/PaginaInicio";
        } else {
            return "redirect:/vista/usuarios";
        }
    }
}
