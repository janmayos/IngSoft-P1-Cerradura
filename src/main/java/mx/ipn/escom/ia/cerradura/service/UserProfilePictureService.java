package mx.ipn.escom.ia.cerradura.service;


import mx.ipn.escom.ia.cerradura.model.UserProfilePicture;
import mx.ipn.escom.ia.cerradura.repository.UserProfilePictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfilePictureService {
    @Autowired
    private UserProfilePictureRepository repository;

    public UserProfilePicture getProfilePicture(Long userId) {
        return repository.findByUsuarioIdUsuario(userId);
    }

    public UserProfilePicture saveProfilePicture(UserProfilePicture profilePicture) {
        return repository.save(profilePicture);
    }

    public void deleteProfilePicture(Long userId) {
        UserProfilePicture profilePicture = repository.findByUsuarioIdUsuario(userId);
        if (profilePicture != null) {
            repository.delete(profilePicture);
        }
    }
}