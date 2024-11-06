package mx.ipn.escom.ia.cerradura.response;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;

public class JwtResponse {

    private String token;
    private UsuarioDTO usuarioDTO;

    public JwtResponse(String token, UsuarioDTO usuarioDTO) {
        this.token = token;
        this.usuarioDTO = usuarioDTO;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }
}
