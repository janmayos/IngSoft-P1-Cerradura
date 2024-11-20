package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.RolService;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@Controller
@RequestMapping("/vista/usuarios")
public class UsuarioVistasController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private RolService rolService;
    @Autowired
    private RolRepository rolRepository;

    // Endpoint para obtener todos los usuarios
    // Endpoint para obtener todos los usuarios
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping({"", "/"})
    public String getClients(Model model, @RequestParam(name = "id", required = false, defaultValue = "0") Long id) {
        if (id == 0) {
            return "redirect:/formlogin";
        }
        List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios();
        Usuario usuarioActual = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuarios", listaUsuarios);
        model.addAttribute("usuarioActual", usuarioActual);
        return "Usuarios/tabla";
    }

    // Endpoint para mostrar la vista de edición de un usuario específico desde la tabla
    
    @GetMapping("/editar/{id}")
    public String obtenerUsuarioPorIdDesdeTabla(@PathVariable(required = false) Long id, @RequestParam(name = "currentUserId", required = false, defaultValue = "0")  Long currentUserId, Model model) {
        if (id == null || id == 0 || currentUserId == null || currentUserId == 0) {
            return "redirect:/formlogin";
        }
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            List<Rol> todosLosRoles = rolService.obtenerTodosLosRoles();
            model.addAttribute("usuario", usuario);
            model.addAttribute("todosLosRoles", todosLosRoles);
            model.addAttribute("currentUserId", currentUserId);
            return "Usuarios/editarTabla";
        } catch (IllegalArgumentException e) {
            return "redirect:/formlogin";
        }
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

        return "redirect:/admin/usuarios";
    }

    // Endpoint para actualizar un usuario
    //@PreAuthorize("hasRole('ROLE_ADMIN')")  
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

        return "redirect:/admin/usuarios";
    }

    // Endpoint para actualizar el usuario actual desde la página de inicio
    @PostMapping("/editarInicio/{id}")
    public ResponseEntity<?> actualizarUsuarioDesdeInicio(
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
            return ResponseEntity.badRequest().body("Error en los datos del formulario");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarUsuario(
        @PathVariable Long id, 
        @Validated @RequestBody RegisterRequest usuarioActualizado, 
        BindingResult result) {

        // if (result.hasErrors()) {
        //     List<String> errors = result.getAllErrors().stream()
        //             .map(DefaultMessageSourceResolvable::getDefaultMessage)
        //             .collect(Collectors.toList());
        //     return ResponseEntity.badRequest().body(errors);
        // }
        Usuario datosusuario = new Usuario();
        datosusuario.setNombre(usuarioActualizado.getNombre());
        datosusuario.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
        datosusuario.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
        datosusuario.setCorreo(usuarioActualizado.getCorreo());
        datosusuario.setUsername(usuarioActualizado.getUsername());
        datosusuario.setPassword(usuarioActualizado.getPassword());
        
        System.out.println("Afuera");
        System.out.println(datosusuario.getPassword());
        
        datosusuario.setEdad(usuarioActualizado.getEdad());
        datosusuario.setGenero(usuarioActualizado.getGenero());
        Set<Rol> roles = new HashSet<>();
        if (usuarioActualizado.getRoles() == null) {
            Optional<Rol> existingRol = rolRepository.findByNombre("ROLE_USER");
            existingRol.ifPresent(roles::add);
        } else {
            for (Rol rol : usuarioActualizado.getRoles()) {
                Rol existingRol = rolRepository.findByNombre(rol.getNombre())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getNombre()));
                roles.add(existingRol);
            }
        }
        datosusuario.setRoles(roles);

        try {
            Usuario usuario = usuarioService.actualizarUsuario(id, datosusuario);
            return ResponseEntity.ok("Usuario actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
