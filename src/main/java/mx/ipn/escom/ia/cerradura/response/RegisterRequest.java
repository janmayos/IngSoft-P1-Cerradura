package mx.ipn.escom.ia.cerradura.response;
import com.fasterxml.jackson.annotation.JsonProperty;


public record RegisterRequest(
    @JsonProperty("access_token")
    String token,
    @JsonProperty("refresh_token")
    String refresh_token

) {

}
