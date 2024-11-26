package mx.ipn.escom.ia.cerradura.controller;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Cambiado a ApacheHttpTransport
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.RequiredArgsConstructor;
import mx.ipn.escom.ia.cerradura.jwt.JwtService;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.AuthService;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestParam String userU, @RequestParam String passwordU) {
        LoginRequest request = new LoginRequest(userU, passwordU);
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> response) {
        try {
            String idTokenString = response.get("id_token");

            if (idTokenString == null || idTokenString.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID token is missing.");
            }

            // Inicializar transport y jsonFactory
            NetHttpTransport transport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();

            // Verificar el token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList("226251044074-or4u73qcs17bj0k70jli3pia63lo1d5q.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Obtener información del usuario
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                System.out.println(email);
                if (email == null || name == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token payload.");
                }

                // Crear un objeto RegisterRequest
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setNombre(name);
                registerRequest.setUsername(name+"Google");
                registerRequest.setCorreo(email);
                registerRequest.setPassword(userId);
                registerRequest.setApellidoMaterno("");
                registerRequest.setApellidoPaterno("");
                registerRequest.setEdad(0);
                registerRequest.setGenero("Masculino");
                 // Puedes usar el userId como contraseña temporal o generar una aleatoria
                //registerRequest.setPictureUrl(pictureUrl);

                // Verificar si el usuario ya existe en la base de datos
                Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
                if (usuario != null) {
                    // Si el usuario ya existe, iniciar sesión
                    return authService.loginGoogle(email);
                } else {
                    // Si el usuario no existe, registrar el nuevo usuario
                    return authService.register(registerRequest);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
