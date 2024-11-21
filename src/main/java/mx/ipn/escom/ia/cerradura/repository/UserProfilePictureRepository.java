package mx.ipn.escom.ia.cerradura.repository;

import mx.ipn.escom.ia.cerradura.model.UserProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfilePictureRepository extends JpaRepository<UserProfilePicture, Long> {
    UserProfilePicture findByUsuarioIdUsuario(Long idUsuario);

}