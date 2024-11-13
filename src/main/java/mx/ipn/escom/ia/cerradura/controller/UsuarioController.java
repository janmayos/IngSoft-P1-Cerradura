package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para obtener un usuario por username
    @GetMapping("/username")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@RequestParam String user) {
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(user);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

  

    // Endpoint para eliminar un usuario por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Validated @RequestBody Usuario usuarioActualizado, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Usuario usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (DataIntegrityViolationException ex) {
            // Añadir errores específicos para username y correo duplicados
            List<String> errors = new ArrayList<>();
            if (ex.getMessage().contains("userU")) {
                errors.add("El nombre de usuario ya está en uso.");
            }
            if (ex.getMessage().contains("correo")) {
                errors.add("El correo ya está en uso.");
            }
            return ResponseEntity.badRequest().body(errors);
        }
    }
    
    
    
    
}
