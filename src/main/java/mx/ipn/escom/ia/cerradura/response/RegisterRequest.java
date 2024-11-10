package mx.ipn.escom.ia.cerradura.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.ipn.escom.ia.cerradura.model.Rol;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    String username;
    String password;
    String nombre;
    String apellidoPaterno;
    String apellidoMaterno;
    String correo;
    Integer edad;
    String genero;
    Set<Rol> roles;
}
