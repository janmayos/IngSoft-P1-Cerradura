package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // @GetMapping("/buscar")
    // public ResponseEntity<List<Usuario>> buscarUsuarios(
    //         @RequestParam(required = false) String nombre,
    //         @RequestParam(required = false) String correo,
    //         @RequestParam(required = false) Integer edad
    // ) {
    //     List<Usuario> usuarios = usuarioService.buscarUsuarios(nombre, correo, edad);
    //     return ResponseEntity.ok(usuarios);
    // }


    //Endpoint para obtener un usuario por ID
    
    @GetMapping("username")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@RequestParam String user) {
        System.out.println(user);
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(user);
        return ResponseEntity.ok(usuario);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        System.out.println(id);
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para actualizar un usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody RegisterRequest request) {
        //Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, request);
        //return ResponseEntity.ok(usuarioActualizado);
        return ResponseEntity.ok(request);
    }
}
