package mx.ipn.escom.ia.cerradura.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios/registro")
public class UsuarioRegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired

    private RolRepository rolRepository;

    

    @PreAuthorize("hasRole('ROLE_ADMIN')")    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setApellidoPaterno(request.getApellidoPaterno());
            usuario.setApellidoMaterno(request.getApellidoMaterno());
            usuario.setCorreo(request.getCorreo());
            usuario.setUsername(request.getUsername());
            usuario.setPassword(request.getPassword());
            usuario.setEdad(request.getEdad());
            usuario.setGenero(request.getGenero());

            Set<Rol> roles = new HashSet<>();
            if (request.getRoles() == null) {
                Optional<Rol> existingRol = rolRepository.findByNombre("ROLE_USER");
                existingRol.ifPresent(roles::add);
            } else {
                for (Rol rol : request.getRoles()) {
                    Rol existingRol = rolRepository.findByNombre(rol.getNombre())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getNombre()));
                    roles.add(existingRol);
                }
            }
            usuario.setRoles(roles);

            usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage()));
        }
    }
}