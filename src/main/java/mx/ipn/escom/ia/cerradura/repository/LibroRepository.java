package mx.ipn.escom.ia.cerradura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.ipn.escom.ia.cerradura.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {
}
