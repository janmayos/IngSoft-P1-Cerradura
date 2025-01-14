package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.jwt.JwtService;
import mx.ipn.escom.ia.cerradura.model.UserProfilePicture;
import mx.ipn.escom.ia.cerradura.service.UserProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile-picture-blob")
public class UserProfilePictureBlobController {

    @Autowired
    private UserProfilePictureService service;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<byte[]> getProfilePictureBlob(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String jwtToken = token.substring(7);
        Long userId = jwtService.getUserIdFromToken(jwtToken);

        UserProfilePicture profilePicture = service.getProfilePicture(userId);
        if (profilePicture != null && profilePicture.getPicture() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/octet-stream");
            return new ResponseEntity<>(profilePicture.getPicture(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<byte[]> getProfilePictureBlobById(@RequestParam Long userId) {
        UserProfilePicture profilePicture = service.getProfilePicture(userId);
        if (profilePicture != null && profilePicture.getPicture() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/octet-stream");
            return new ResponseEntity<>(profilePicture.getPicture(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
