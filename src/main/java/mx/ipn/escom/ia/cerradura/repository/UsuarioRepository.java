package mx.ipn.escom.ia.cerradura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.ipn.escom.ia.cerradura.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsername(String userU);
}