package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import mx.ipn.escom.ia.cerradura.model.Rol;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;
import mx.ipn.escom.ia.cerradura.repository.RolRepository;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


abstract class BaseController {
    @ModelAttribute("Usuario")
    public UsuarioDTO populateUsuario() {
        return new UsuarioDTO(); // Crea un nuevo objeto UsuarioDTO o lo recupera de la sesión
    }
}

@Controller
@RequestMapping("/auth")
@SessionAttributes("Usuario")
public class AuthController extends BaseController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository; // <-- Asegúrate de inyectar el repositorio de roles

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Usuario usuario) {
        // Codificar la contraseña si usas un password encoder
        //usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setPassword(usuario.getPassword());
        
        Set<Rol> roles = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            // Buscar el rol en la base de datos
            Rol existingRol = rolRepository.findByNombre(rol.getNombre()) // <-- Ahora está correctamente inyectado
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getNombre()));
            roles.add(existingRol);
        }
        
        // Asignar roles al usuario
        usuario.setRoles(roles);
        
        // Guardar el usuario
        usuarioRepository.save(usuario);
        
        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
    }



    @PostMapping("/login")
    //@ModelAttribute("Usuario")
    public ResponseEntity<UsuarioDTO> loginUser(@RequestParam(name="userU",required = true) String nombreUsuario,
    @RequestParam(name="passwordU",required = true) String passwordUsuario,
    Model model
    ) {
        
        Usuario usuario = new Usuario();
        usuario.setUsername(nombreUsuario);
        usuario.setPassword(passwordUsuario);
        Optional<Usuario> foundUser = usuarioRepository.findByUsername(usuario.getUsername());
        if (foundUser.isPresent() && usuario.getPassword().matches(foundUser.get().getPassword()) ){//passwordEncoder.matches(usuario.getPassword(), foundUser.get().getPassword())) {
            
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombre(foundUser.get().getNombre());
            //zstatus.setComplete();
            System.out.println(usuarioDTO);
            System.out.println(usuarioDTO.getNombre());
            model.addAttribute("Usuario", usuarioDTO);
            
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        }else{
            System.out.println( foundUser.get().getPassword());
            System.out.println( usuario.getPassword());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }


}