package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@RestController
@RequestMapping("/libros")
public class LibrosController {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @GetMapping("/buscar")
    public ResponseEntity<String> buscarPorTitulo(@RequestParam String titulo) throws IOException, InterruptedException {
        String url = "https://openlibrary.org/search.json?q=" + titulo.replace(" ", "+");
        return realizarBusqueda(url);
    }

    @GetMapping("/buscarPorTitulo")
    public ResponseEntity<String> buscarTitulo(@RequestParam String titulo) throws IOException, InterruptedException {
        String url = "https://openlibrary.org/search.json?title=" + titulo.replace(" ", "+");
        return realizarBusqueda(url);
    }

    @GetMapping("/buscarPorAutor")
    public ResponseEntity<String> buscarPorAutor(@RequestParam String autor) throws IOException, InterruptedException {
        String url = "https://openlibrary.org/search.json?author=" + autor.replace(" ", "+") + "&sort=new";
        return realizarBusqueda(url);
    }

    @GetMapping("/buscarPaginado")
    public ResponseEntity<String> buscarPaginado(@RequestParam String query, @RequestParam int page) throws IOException, InterruptedException {
        String url = "https://openlibrary.org/search.json?q=" + query.replace(" ", "+") + "&page=" + page;
        return realizarBusqueda(url);
    }

    private ResponseEntity<String> realizarBusqueda(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al obtener datos de la API", HttpStatus.BAD_REQUEST);
        }
    }
}
