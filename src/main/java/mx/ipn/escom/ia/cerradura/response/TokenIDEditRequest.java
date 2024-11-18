package mx.ipn.escom.ia.cerradura.response;

public class TokenIDEditRequest {
    private String token;
    private Long id;

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}