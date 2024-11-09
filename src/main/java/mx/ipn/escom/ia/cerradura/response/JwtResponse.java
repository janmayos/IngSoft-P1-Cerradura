package mx.ipn.escom.ia.cerradura.response;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;

public class JwtResponse {

    private String token;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public JwtResponse(String token, String nombre,String apellidoPaterno, String apellidoMaterno) {
        this.token = token;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

}   
