package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vista/usuarios")
public class UsuarioVistasController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para obtener todos los usuarios
    @GetMapping({"", "/"})
    public String getClients(Model model) {
        List<Usuario> listaUsuarios = usuarioRepository.findAll(Sort.by(Sort.Direction.DESC, "idUsuario")); // Cambiar "id" a "idUsuario"
        model.addAttribute("usuarios", listaUsuarios);
        return "Usuarios/tabla";
    }

    // Endpoint para mostrar la vista de edición de un usuario específico
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            return "Usuarios/editar"; // Nombre de la plantilla Thymeleaf para la vista de edición
        } else {
            return "redirect:/vista/usuarios"; // Redirige a la lista de usuarios si el usuario no se encuentra
        }
    }

    // Endpoint para actualizar los datos del usuario
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable("id") Long id, @ModelAttribute("usuario") Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuarioExistente = usuarioOpt.get();
            // Actualizar los datos del usuario existente con los nuevos datos
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
            usuarioExistente.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
            usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
            usuarioExistente.setEdad(usuarioActualizado.getEdad());
            usuarioExistente.setGenero(usuarioActualizado.getGenero());
            usuarioExistente.setRoles(usuarioActualizado.getRoles());

            // Guardar los cambios en la base de datos
            usuarioRepository.save(usuarioExistente);
        }
        return "redirect:/vista/usuarios"; // Redirige a la lista de usuarios después de actualizar
    }
}
