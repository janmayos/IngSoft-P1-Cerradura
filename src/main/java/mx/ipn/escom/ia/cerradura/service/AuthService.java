package mx.ipn.escom.ia.cerradura.service;

import mx.ipn.escom.ia.cerradura.jwt.JwtService;
import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import mx.ipn.escom.ia.cerradura.response.AuthResponse;
import mx.ipn.escom.ia.cerradura.response.ErrorResponse;
import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository userRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> login(LoginRequest request) {
        
        Optional<Usuario> foundUser = userRepository.findByUsername(request.getUsername());
        System.out.println(foundUser.isPresent());
        if (foundUser.isPresent() && passwordEncoder.matches(request.getPassword(), foundUser.get().getPassword())) {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = foundUser.orElseThrow();
            HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("nombre", foundUser.get().getNombre());
            String token = jwtService.getToken(claims,user);

            return ResponseEntity.ok().body(AuthResponse.builder()
                    .token(token)
                    .nombre(foundUser.get().getNombre())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().msg("Error usuario y/o contraseña invalidad").build());
        

    }

    public ResponseEntity<?> register(RegisterRequest request) {
        Usuario user = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .correo(request.getCorreo())
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .edad(request.getEdad())
                .genero(request.getGenero())
                .roles(null)

                .build();
        Set<Rol> roles = new HashSet<>();
        for (Rol rol : request.getRoles()) {
            // Buscar el rol en la base de datos
            Rol existingRol = rolRepository.findByNombre(rol.getNombre()) // <-- Ahora está correctamente inyectado
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getNombre()));
            roles.add(existingRol);
        }
        user.setRoles(roles);


        try {
            // Código que interactúa con la base de datos (por ejemplo, guardar una entidad)
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Capturar excepciones de violación de integridad de datos (más genérica en Spring Data)
            System.out.println("Error de integridad de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().msg("Error al crear usuario").build());
        } catch (PersistenceException e) {
            // Capturar excepciones específicas de JPA
            System.out.println("Error de persistencia: " + e.getMessage());
        } catch (Exception e) {
            // Capturar cualquier otra excepción
            System.out.println("Error general: " + e.getMessage());
        }

        return ResponseEntity.ok().body(AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build());

    }

    /*@PostMapping("/register")
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
    } */

}
