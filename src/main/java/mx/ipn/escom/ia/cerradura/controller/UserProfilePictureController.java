package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.UserProfilePicture;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.service.UserProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile-picture")
public class UserProfilePictureController {
    @Autowired
    private UserProfilePictureService service;

    @GetMapping("/{userId}")
    public UserProfilePicture getProfilePicture(@PathVariable Long userId) {
        return service.getProfilePicture(userId);
    }

    @PostMapping("/{userId}")
    public UserProfilePicture uploadProfilePicture(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {
        UserProfilePicture profilePicture = new UserProfilePicture();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(userId);
        profilePicture.setUsuario(usuario);
        profilePicture.setPicture(file.getBytes());
        return service.saveProfilePicture(profilePicture);
    }
}