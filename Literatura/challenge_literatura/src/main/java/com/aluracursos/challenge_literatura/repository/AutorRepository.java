package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Autor}.
 *
 * <p>Provee operaciones CRUD y consultas personalizadas sobre la tabla de autores.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    /**
     * Busca un autor por nombre exacto (case-insensitive).
     *
     * @param nombre nombre a buscar
     * @return un {@link Optional} con el autor si existe
     */
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    /**

     *
     * @param anio año de consulta
     * @return lista de autores que estaban vivos ese año
     */
    @Query("""
           SELECT a FROM Autor a
           WHERE (a.anioNacimiento IS NULL OR a.anioNacimiento <= :anio)
             AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)
           ORDER BY a.nombre
           """)
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    /**
     * Retorna todos los autores ordenados alfabéticamente por nombre.
     */
    @Query("SELECT a FROM Autor a ORDER BY a.nombre")
    List<Autor> findAllOrderByNombre();

    /**
     * Busca autores cuyo nombre contiene la cadena dada (case-insensitive).
     *
     * @param fragmento parte del nombre a buscar
     * @return lista de autores que coinciden
     */
    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :fragmento, '%'))")
    List<Autor> findByNombreContaining(@Param("fragmento") String fragmento);
}
