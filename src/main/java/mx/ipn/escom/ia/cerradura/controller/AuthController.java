package mx.ipn.escom.ia.cerradura.controller;

import java.util.Collections;
import java.util.Map;

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

            // Inicializar transport y jsonFactory
            NetHttpTransport transport = new NetHttpTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            // Configurar el verificador de token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList("226251044074-or4u73qcs17bj0k70jli3pia63lo1d5q.apps.googleusercontent.com")) // Reemplaza con tu Client ID
                    .build();

            // Verificar el token
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Obtener datos del usuario
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                // Procesar información del usuario
                System.out.println("User ID: " + userId);
                System.out.println("Email: " + email);
                System.out.println("Name: " + name);
                System.out.println("direccion de imagen"+ pictureUrl);

                return ResponseEntity.ok("Google login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid ID token");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error verifying Google ID token");
        }







        // try {
        //     // Validar token de Google
        //     GoogleIdToken.Payload payload = googleVerify(idToken);

        //     String nombre = (String) payload.get("name");
        //     String img = (String) payload.get("picture");
        //     String correo = (String) payload.get("email");

        //     GoogleIdToken idToken = verifier.verify(token);
        //     if (idToken != null) {
        //         GoogleIdToken.Payload userInfo = idToken.getPayload();

        //         String email = userInfo.getEmail();
        //         String nombre = (String) userInfo.get("name");
        //         String googleId = userInfo.getSubject();

        //         // Buscar o registrar usuario
        //         Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(email);
        //         if (usuario == null) {
        //             usuario = new Usuario();
        //             usuario.setCorreo(email);
        //             usuario.setNombre(nombre);
        //             usuario.setUsername(email);
        //             usuarioService.registrarUsuario(usuario);
        //         }

        //         // Generar un token JWT para el usuario
        //         String jwtToken = authService.generarToken(usuario);
        //         return ResponseEntity.ok(Map.of("success", true, "token", jwtToken));
        //     } else {
        //         return ResponseEntity.status(401).body("Token de Google no válido.");
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return ResponseEntity.status(500).body("Error al procesar el inicio de sesión con Google.");
        // }
    }
}
