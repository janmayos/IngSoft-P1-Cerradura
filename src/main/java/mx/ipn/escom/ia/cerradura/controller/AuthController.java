package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository; // <-- Asegúrate de inyectar el repositorio de roles

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Usuario usuario) {
        // Codificar la contraseña si usas un password encoder
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        Set<Rol> roles = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            // Buscar el rol en la base de datos
            Rol existingRol = rolRepository.findByNombre(rol.getNombre()) // <-- Ahora está correctamente inyectado
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getNombre()));
            roles.add(existingRol);
        }
        
        // Asignar roles al usuario
        usuario.setRoles(roles);
        
        // Guardar el usuario
        usuarioRepository.save(usuario);
        
        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public String loginUser(@RequestBody Usuario usuario) {
        Optional<Usuario> foundUser = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (foundUser.isPresent() && passwordEncoder.matches(usuario.getPassword(), foundUser.get().getPassword())) {
            return "Inicio de sesión exitoso";
        }
        return "Credenciales inválidas";
    }


}