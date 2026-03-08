package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Idioma;
import com.aluracursos.challenge_literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Libro}.
 *
 * <p>Extiende {@link JpaRepository} para obtener las operaciones CRUD básicas
 * y define consultas personalizadas mediante JPQL.
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    /**
     * Busca un libro por título exacto (case-insensitive).
     *
     * @param titulo título a buscar
     * @return un {@link Optional} con el libro si existe
     */
    Optional<Libro> findByTituloIgnoreCase(String titulo);


    List<Libro> findByIdioma(Idioma idioma);


    @Query("SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC")
    List<Libro> findAllOrderByDescargasDesc();


    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :fragmento, '%'))")
    List<Libro> findByTituloContaining(@Param("fragmento") String fragmento);

    /**
     * Cuenta cuántos libros hay registrados por idioma.
     *
     * @return lista de arreglos Object[] con [Idioma, Long count]
     */
    @Query("SELECT l.idioma, COUNT(l) FROM Libro l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
    List<Object[]> countByIdioma();
}
