package mx.ipn.escom.ia.cerradura.response;
import com.fasterxml.jackson.annotation.JsonProperty;


public record LoginRequest(
    @JsonProperty("usuario")
    String usuario,
    @JsonProperty("password")
    String password

) {

}
