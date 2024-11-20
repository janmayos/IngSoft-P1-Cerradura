package mx.ipn.escom.ia.cerradura.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InicioRequest {
    private String token;
}