package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport; // Cambiado a ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.AuthService;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

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
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        try {
            // Usar ApacheHttpTransport en lugar de NetHttpTransport
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("226251044074-or4u73qcs17bj0k70jli3pia63lo1d5q.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload userInfo = idToken.getPayload();

                String email = userInfo.getEmail();
                String nombre = (String) userInfo.get("name");
                String googleId = userInfo.getSubject();

                // Buscar o registrar usuario
                Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setCorreo(email);
                    usuario.setNombre(nombre);
                    usuario.setUsername(email);
                    usuarioService.registrarUsuario(usuario);
                }

                // Generar un token JWT para el usuario
                String jwtToken = authService.generarToken(usuario);
                return ResponseEntity.ok(Map.of("success", true, "token", jwtToken));
            } else {
                return ResponseEntity.status(401).body("Token de Google no válido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al procesar el inicio de sesión con Google.");
        }
    }
}
