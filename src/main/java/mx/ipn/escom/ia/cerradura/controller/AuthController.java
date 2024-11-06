package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.AuthenticationManager;


import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import mx.ipn.escom.ia.cerradura.jwt.JwtUtils;
import mx.ipn.escom.ia.cerradura.response.JwtResponse;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;



@RestController
@RequestMapping("/auth")
public class AuthController  {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

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
    public ResponseEntity<?> login(@RequestParam String userU, @RequestParam String passwordU) {
        Optional<Usuario> foundUser = usuarioRepository.findByUsername(userU);
        
        if (foundUser.isPresent() && passwordEncoder.matches(passwordU, foundUser.get().getPassword())) {
            // Crear el UsuarioDTO a partir de Usuario
            Usuario usuario = foundUser.get();
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellidoPaterno(usuario.getApellidoPaterno());
            usuarioDTO.setApellidoMaterno(usuario.getApellidoMaterno());
            usuarioDTO.setCorreo(usuario.getCorreo());
            usuarioDTO.setEdad(usuario.getEdad());
            usuarioDTO.setGenero(usuario.getGenero());
            usuarioDTO.setRoles(usuario.getRoles());
    
            // Generar el token
            String token = jwtUtils.generateToken(usuarioDTO);
    
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }



}