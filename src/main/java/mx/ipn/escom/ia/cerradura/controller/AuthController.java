package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.response.InicioRequest;
import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.AuthService;
import mx.ipn.escom.ia.cerradura.service.UsuarioService;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/inicio")
    public ResponseEntity<?> cargarPaginaInicio(@RequestBody InicioRequest request) {
        String token = request.getToken();
        Long userId = request.getUserId();

        // Verificar el token y cargar la información del usuario
        Usuario usuario = usuarioService.obtenerUsuarioPorId(userId);

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
