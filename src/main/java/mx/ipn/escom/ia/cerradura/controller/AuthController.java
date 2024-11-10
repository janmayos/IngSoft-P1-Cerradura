package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.ipn.escom.ia.cerradura.response.AuthResponse;

import mx.ipn.escom.ia.cerradura.response.LoginRequest;
import mx.ipn.escom.ia.cerradura.response.RegisterRequest;
import mx.ipn.escom.ia.cerradura.service.AuthService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController  {

    private final AuthService authService;

    
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestParam String userU, @RequestParam String passwordU ){
        LoginRequest request = new LoginRequest(userU, passwordU);
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }



}