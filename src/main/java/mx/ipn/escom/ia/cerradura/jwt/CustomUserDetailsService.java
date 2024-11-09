package mx.ipn.escom.ia.cerradura.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import mx.ipn.escom.ia.cerradura.model.Usuario;
import mx.ipn.escom.ia.cerradura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(usuario.getRoles().stream()
                        .map(rol -> "ROLE_" + rol.getNombre())  // Prefijar con "ROLE_"
                        .collect(Collectors.toList())
                        .toArray(new String[0]))
                .build();
    }
}