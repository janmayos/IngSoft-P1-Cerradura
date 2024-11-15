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
    public String getClients(Model model, @RequestParam("id") Long id) {
        List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios();
        Usuario usuarioActual = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuarios", listaUsuarios);
        model.addAttribute("usuarioActual", usuarioActual);
        return "Usuarios/tabla";
    }

    // Endpoint para mostrar la vista de edición de un usuario específico desde la tabla
    @GetMapping("/editar/{id}")
    public String obtenerUsuarioPorIdDesdeTabla(@PathVariable Long id, @RequestParam("currentUserId") Long currentUserId, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosRoles", todosLosRoles);
        model.addAttribute("currentUserId", currentUserId);
        return "Usuarios/editarTabla";
    }

    // Endpoint para mostrar la vista de edición del usuario actual desde la página de inicio
    @GetMapping("/editarInicio/{id}")
    public String obtenerUsuarioPorIdDesdeInicio(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosRoles", todosLosRoles);
        model.addAttribute("currentUserId", id);
        return "Usuarios/editarInicio";
    }

    // Endpoint para mostrar la vista de registro de un nuevo usuario
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(@RequestParam("currentUserId") Long currentUserId, Model model) {
        List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("todosLosRoles", todosLosRoles);
        model.addAttribute("currentUserId", currentUserId);
        return "Usuarios/registrarUsuario";
    }

    // Endpoint para procesar el registro de un nuevo usuario
    @PostMapping("/registrar")
    public String registrarUsuario(@Validated @ModelAttribute("usuario") Usuario usuario, BindingResult result, @RequestParam(value = "roles", required = false) List<Long> rolesIds, @RequestParam("currentUserId") Long currentUserId, Model model) {
        if (result.hasErrors()) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("currentUserId", currentUserId);
            return "Usuarios/registrarUsuario";
        }

        Set<Rol> rolesSet = rolesIds != null ? rolesIds.stream().map(rolService::obtenerRolPorId).collect(Collectors.toSet()) : Set.of();
        usuario.setRoles(rolesSet);

        try {
            usuarioService.registrarUsuario(usuario);
        } catch (IllegalArgumentException e) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("currentUserId", currentUserId);
            return "Usuarios/registrarUsuario";
        }

        return "redirect:/vista/usuarios?id=" + currentUserId;
    }

    // Endpoint para actualizar un usuario
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(
        @PathVariable Long id, 
        @Validated @ModelAttribute("usuario") Usuario usuarioActualizado, 
        BindingResult result, 
        @RequestParam(value = "roles", required = false) List<Long> rolesIds, 
        @RequestParam(value = "currentUserId") Long currentUserId,
        Model model) {
        
        if (result.hasErrors()) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("currentUserId", currentUserId);
            return "Usuarios/editarTabla";
        }

        Set<Rol> rolesSet = rolesIds != null ? rolesIds.stream().map(rolService::obtenerRolPorId).collect(Collectors.toSet()) : Set.of();
        usuarioActualizado.setRoles(rolesSet);

        try {
            usuarioService.actualizarUsuario(id, usuarioActualizado);
        } catch (IllegalArgumentException e) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("currentUserId", currentUserId);
            return "Usuarios/editarTabla";
        }

        return "redirect:/vista/usuarios?id=" + currentUserId;
    }

    // Endpoint para actualizar el usuario actual desde la página de inicio
    @PostMapping("/editarInicio/{id}")
    public String actualizarUsuarioDesdeInicio(
        @PathVariable Long id, 
        @Validated @ModelAttribute("usuario") Usuario usuarioActualizado, 
        BindingResult result, 
        @RequestParam(value = "roles", required = false) List<Long> rolesIds, 
        Model model) {
            
        if (result.hasErrors()) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("currentUserId", id);
            return "Usuarios/editarInicio";
        }
        Set<Rol> rolesSet = rolesIds != null ? rolesIds.stream().map(rolService::obtenerRolPorId).collect(Collectors.toSet()) : Set.of();
        usuarioActualizado.setRoles(rolesSet);
        try {
            usuarioService.actualizarUsuario(id, usuarioActualizado);
        } catch (IllegalArgumentException e) {
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuarioActualizado);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("currentUserId", id);
            return "Usuarios/editarInicio";
        }
        return "redirect:/PaginaInicio?id=" + id;
    }
}
