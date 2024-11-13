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
        usuarioExistente.setNombre(detallesUsuario.getNombre());
        usuarioExistente.setApellidoPaterno(detallesUsuario.getApellidoPaterno());
        usuarioExistente.setApellidoMaterno(detallesUsuario.getApellidoMaterno());
        usuarioExistente.setCorreo(detallesUsuario.getCorreo());
        usuarioExistente.setUsername(detallesUsuario.getUsername());
        if(passwordEncoder.matches(usuarioExistente.getPassword(), usuarioExistente.getPassword())){
            usuarioExistente.setPassword(detallesUsuario.getPassword());
        }else{
            usuarioExistente.setPassword(passwordEncoder.encode(detallesUsuario.getPassword()));
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
