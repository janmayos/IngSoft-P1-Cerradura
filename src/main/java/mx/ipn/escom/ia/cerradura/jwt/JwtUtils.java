package mx.ipn.escom.ia.cerradura.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;


import mx.ipn.escom.ia.cerradura.response.JwtResponse;
import mx.ipn.escom.ia.cerradura.model.UsuarioDTO;
import mx.ipn.escom.ia.cerradura.model.Rol;


@Component
public class JwtUtils {
    private String SECRET_KEY = "b87c504a2ffea6d2c947c19784b0d1c98f3df1fcd6ca5a2ee1cda0b01b86307b"; // Cambia esto a una clave segura

    // Generar un token JWT
    public String generateToken(UsuarioDTO usuarioDTO) {
        // Generar el token
        return Jwts.builder()
                .setSubject(usuarioDTO.getNombre())  // Usamos el nombre del usuario como el subject
                .claim("nombre", usuarioDTO.getNombre())  // Agregar nombre
                .claim("apellidoPaterno", usuarioDTO.getApellidoPaterno())  // Agregar apellido paterno
                .claim("apellidoMaterno", usuarioDTO.getApellidoMaterno())  // Agregar apellido materno
                .claim("correo", usuarioDTO.getCorreo())  // Agregar correo
                .claim("edad", usuarioDTO.getEdad())  // Agregar edad
                .claim("genero", usuarioDTO.getGenero())  // Agregar genero
                .claim("roles", usuarioDTO.getRoles().stream()
                        .map(Rol::getNombre)  // Extraemos los nombres de los roles
                        .collect(Collectors.toList()))  // Los roles como lista de strings
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira en 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // Firma con HS256 y la clave secreta
                .compact();  // Generar el token JWT

    }
    

    // Extraer el nombre de usuario del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer cualquier dato del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar el token
    public Boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Verificar si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Extraer los roles del token
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);  // Extrae los roles como lista de strings
    }

}
