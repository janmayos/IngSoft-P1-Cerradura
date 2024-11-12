package mx.ipn.escom.ia.cerradura.service;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // public List<Usuario> buscarUsuarios(String nombre, String correo, Integer edad) {
    //     // Ejemplo de una consulta simple combinando los parámetros
    //     if (nombre != null && correo != null && edad != null) {
    //         return usuarioRepository.findByNombreAndCorreoAndEdad(nombre, correo, edad);
    //     } else if (nombre != null && correo != null) {
    //         return usuarioRepository.findByNombreAndCorreo(nombre, correo);
    //     } else if (nombre != null) {
    //         return usuarioRepository.findByNombre(nombre);
    //     } else if (correo != null) {
    //         return usuarioRepository.findByCorreo(correo);
    //     } else if (edad != null) {
    //         return usuarioRepository.findByEdad(edad);
    //     } else {
    //         return usuarioRepository.findAll(); // Devuelve todos los usuarios si no se pasa ningún parámetro
    //     }
    // }

    // Actualizar un usuario
    public Usuario actualizarUsuario(Long id, Usuario detallesUsuario) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(detallesUsuario.getNombre());
            usuarioExistente.setApellidoPaterno(detallesUsuario.getApellidoPaterno());
            usuarioExistente.setApellidoMaterno(detallesUsuario.getApellidoMaterno());
            usuarioExistente.setCorreo(detallesUsuario.getCorreo());
            usuarioExistente.setUsername(detallesUsuario.getUsername());
            usuarioExistente.setPassword(detallesUsuario.getPassword());
            usuarioExistente.setEdad(detallesUsuario.getEdad());
            usuarioExistente.setGenero(detallesUsuario.getGenero());

            // Actualizar roles (verifica que los roles no estén vacíos)
            if (detallesUsuario.getRoles() != null && !detallesUsuario.getRoles().isEmpty()) {
                usuarioExistente.setRoles(detallesUsuario.getRoles());
            }

            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario obtenerUsuarioPorUsername(String user) {
        
        Optional<Usuario> foundUser = usuarioRepository.findByUsername(user);
        System.out.println(foundUser.isPresent());
        if (foundUser.isPresent()){
            return foundUser.get();
        } 
        return null;
    }
}
