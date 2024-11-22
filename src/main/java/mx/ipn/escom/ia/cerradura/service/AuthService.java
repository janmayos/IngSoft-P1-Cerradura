package mx.ipn.escom.ia.cerradura.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mx.ipn.escom.ia.cerradura.jwt.JwtService;
import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import mx.ipn.escom.ia.cerradura.response.AuthResponse;
import mx.ipn.escom.ia.cerradura.response.ErrorResponse;
import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    // Clave secreta para firmar el token JWT
    private final String SECRET_KEY = "your_secret_key"; // Cambia esto por tu clave secreta
    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public ResponseEntity<?> login(LoginRequest request) {
        Optional<Usuario> foundUser = userRepository.findByUsername(request.getUsername());
        if (foundUser.isPresent() && passwordEncoder.matches(request.getPassword(), foundUser.get().getPassword())) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = foundUser.orElseThrow();
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("nombre", foundUser.get().getNombre());
            claims.put("id", foundUser.get().getIdUsuario()); // Incluye el id del usuario
            String token = generarToken(foundUser.get());

            return ResponseEntity.ok().body(AuthResponse.builder()
                    .token(token)
                    .nombre(foundUser.get().getNombre())
                    .id(foundUser.get().getIdUsuario()) // Incluye el id del usuario
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder().msg("Error usuario y/o contraseña invalidad").build());
    }

    public ResponseEntity<?> register(RegisterRequest request) {
        // Validar si el correo ya existe
        if (userRepository.findByCorreo(request.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().msg("El correo ya está en uso").build());
        }

        // Validar si el username ya existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().msg("El nombre de usuario ya está en uso").build());
        }

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
        user.setRoles(roles);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().msg("Error al crear usuario: " + e.getMessage()).build());
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().msg("Error de persistencia").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().msg("Error general").build());
        }

        return ResponseEntity.ok().body(AuthResponse.builder()
                .token(generarToken(user))
                .build());
    }

    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getIdUsuario());
        claims.put("email", usuario.getCorreo());
        claims.put("nombre", usuario.getNombre());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
