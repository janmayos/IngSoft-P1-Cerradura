package mx.ipn.escom.ia.cerradura.controller;

import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vista/usuarios")
public class UsuarioVistasController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para obtener todos los usuarios
    @GetMapping({"","/"})
    public String getClients(Model model){
        List<Usuario> listaUsuarios = usuarioRepository.findAll(Sort.by(Sort.Direction.DESC, "idUsuario")); // Cambiar "id" a "idUsuario"
        model.addAttribute("usuarios", listaUsuarios);
        return "Usuarios/tabla";
        //return listaUsuarios;
    }


}
