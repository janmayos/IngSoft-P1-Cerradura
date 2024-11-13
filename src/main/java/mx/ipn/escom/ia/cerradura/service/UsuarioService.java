package mx.ipn.escom.ia.cerradura.service;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // Actualizar un usuario
    public Usuario actualizarUsuario(Long id, Usuario detallesUsuario) {
        Usuario usuarioExistente = obtenerUsuarioPorId(id);
        
        // Validar que el nuevo correo no esté en uso por otro usuario
        Optional<Usuario> usuarioConMismoCorreo = usuarioRepository.findByCorreo(detallesUsuario.getCorreo());
        if (usuarioConMismoCorreo.isPresent() && !usuarioConMismoCorreo.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("El correo ya está en uso.");
        }

        // Validar que el nuevo username no esté en uso por otro usuario
        Optional<Usuario> usuarioConMismoUsername = usuarioRepository.findByUsername(detallesUsuario.getUsername());
        if (usuarioConMismoUsername.isPresent() && !usuarioConMismoUsername.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("El username ya está en uso.");
        }

        usuarioExistente.setNombre(detallesUsuario.getNombre());
        usuarioExistente.setApellidoPaterno(detallesUsuario.getApellidoPaterno());
        usuarioExistente.setApellidoMaterno(detallesUsuario.getApellidoMaterno());
        usuarioExistente.setCorreo(detallesUsuario.getCorreo());
        usuarioExistente.setUsername(detallesUsuario.getUsername());

        // Solo actualizar la contraseña si se ha proporcionado una nueva
        if (detallesUsuario.getPassword() != null && !detallesUsuario.getPassword().isEmpty()) {
            if (passwordEncoder.matches(detallesUsuario.getPassword(), usuarioExistente.getPassword())) {
                usuarioExistente.setPassword(usuarioExistente.getPassword());
            } else {
                usuarioExistente.setPassword(passwordEncoder.encode(detallesUsuario.getPassword()));
            }
        }

        usuarioExistente.setEdad(detallesUsuario.getEdad());
        usuarioExistente.setGenero(detallesUsuario.getGenero());
        usuarioExistente.setRoles(detallesUsuario.getRoles());
        return usuarioRepository.save(usuarioExistente);
    }

    // Obtener un usuario por username
    public Usuario obtenerUsuarioPorUsername(String user) {
        Optional<Usuario> foundUser = usuarioRepository.findByUsername(user);
        return foundUser.orElse(null);
    }

    // Eliminar un usuario por ID
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
