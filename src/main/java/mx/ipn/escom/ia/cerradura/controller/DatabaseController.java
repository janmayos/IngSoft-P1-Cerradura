package mx.ipn.escom.ia.cerradura.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/check-connection")
    public String checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                return "Conexión exitosa a la base de datos!";
            } else {
                return "No se pudo establecer la conexión.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar a la base de datos: " + e.getMessage();
        }
    }
}
