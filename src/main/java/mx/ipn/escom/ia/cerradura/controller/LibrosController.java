package mx.ipn.escom.ia.cerradura.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.ipn.escom.ia.cerradura.model.Libro;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibrosController {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarPorTituloYAutor(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor) throws IOException, InterruptedException {

        List<Libro> resultados = new ArrayList<>();

        // Búsqueda por título
        if (titulo != null && !titulo.isEmpty()) {
            String urlTitulo = "https://openlibrary.org/search.json?title=" + titulo.replace(" ", "+");
            resultados.addAll(realizarBusqueda(urlTitulo));
        }

        // Búsqueda por autor
        if (autor != null && !autor.isEmpty()) {
            String urlAutor = "https://openlibrary.org/search.json?author=" + autor.replace(" ", "+");
            resultados.addAll(realizarBusqueda(urlAutor));
        }

        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    private List<Libro> realizarBusqueda(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parsearResultados(response.body());
        } else {
            return new ArrayList<>();
        }
    }

    private List<Libro> parsearResultados(String json) throws IOException {
        List<Libro> libros = new ArrayList<>();

        JsonNode root = objectMapper.readTree(json);
        JsonNode docs = root.path("docs");

        for (JsonNode doc : docs) {
            Libro libro = new Libro();

            libro.setBookId(doc.path("key").asText());
            libro.setAuthorId(doc.path("author_key").isArray() ? doc.path("author_key").get(0).asText() : "");
            libro.setAuthorName(doc.path("author_name").isArray() ? doc.path("author_name").get(0).asText() : "");
            libro.setTitle(doc.path("title").asText());
            libro.setFirstPublishYear(doc.path("first_publish_year").asInt(0));

            // Intentar obtener la portada del primer sitio
            String coverUrl = doc.path("cover_i").isMissingNode() ? "" : "https://covers.openlibrary.org/b/id/" + doc.path("cover_i").asText() + "-M.jpg";
            if (coverUrl.isEmpty()) {
                // Intentar obtener la portada del segundo sitio
                coverUrl = doc.path("cover_edition_key").isMissingNode() ? "" : "https://covers.openlibrary.org/b/olid/" + doc.path("cover_edition_key").asText() + "-M.jpg";
            }
            libro.setCoverUrl(coverUrl);

            libros.add(libro);
        }

        return libros;
    }
}
