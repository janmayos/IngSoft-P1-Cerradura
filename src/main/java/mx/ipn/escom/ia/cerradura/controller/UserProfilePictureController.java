package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.UserProfilePicture;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.service.UserProfilePictureService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile-picture")
public class UserProfilePictureController {
    @Autowired
    private UserProfilePictureService service;

    @GetMapping("/data-user")
    public ResponseEntity<UserProfilePicture> getProfilePictureUser(@RequestParam Long userId) {
        UserProfilePicture profilePicture = service.getProfilePicture(userId);
        if (profilePicture != null) {
            return new ResponseEntity<>(profilePicture, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    @GetMapping
    public ResponseEntity<byte[]> getProfilePicture(@RequestParam Long userId) {
        UserProfilePicture profilePicture = service.getProfilePicture(userId);
        if (profilePicture != null && profilePicture.getPicture() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg"); // Ajusta el tipo de contenido según el formato de la imagen
            return new ResponseEntity<>(profilePicture.getPicture(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestParam Long userId, @RequestParam("file") MultipartFile file) {
        try {
            UserProfilePicture profilePicture = new UserProfilePicture();
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(userId);
            profilePicture.setUsuario(usuario);
            profilePicture.setPicture(file.getBytes());
            service.saveProfilePicture(profilePicture);
            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}