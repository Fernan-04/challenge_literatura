package com.aluracursos.challenge_literatura.service;

import com.aluracursos.challenge_literatura.model.*;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio principal del catálogo LiterAlura.
 *
 */
@Service
public class LibroService {

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private ConsumoApi consumoApi;

    @Autowired
    private ConvierteDatos convierteDatos;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    // ─── Búsqueda y persistencia ──────────────────────────────────────────────

    /**
     * Busca un libro por título en la API Gutendex, toma el primer resultado
     * y lo persiste en la base de datos si no existe previamente.
     *
     * @param titulo título (o fragmento) del libro a buscar
     * @return un {@link Optional} con el libro guardado o vacío si no se encontró
     */
    @Transactional
    public Optional<Libro> buscarYGuardarLibro(String titulo) {
        String url = URL_BASE + titulo.replace(" ", "+");

        try {
            String json = consumoApi.obtenerDatos(url);
            RespuestaApiRecord respuesta = convierteDatos.convertir(json, RespuestaApiRecord.class);

            if (respuesta.resultados() == null || respuesta.resultados().isEmpty()) {
                return Optional.empty();
            }

            LibroRecord primerResultado = respuesta.resultados().get(0);

            // Verificar si el libro ya está registrado
            Optional<Libro> libroExistente = libroRepository
                    .findByTituloIgnoreCase(primerResultado.titulo());

            if (libroExistente.isPresent()) {
                System.out.println("\n  ⚠  El libro \"" + primerResultado.titulo()
                        + "\" ya está registrado en el catálogo.");
                return libroExistente;
            }

            // Construir y persistir el libro
            Libro nuevoLibro = construirLibroConAutorGestionado(primerResultado);
            Libro guardado = libroRepository.save(nuevoLibro);
            return Optional.of(guardado);

        } catch (RuntimeException e) {
            System.err.println("\n  ✗ Error al consultar la API: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Construye un {@link Libro} reutilizando el {@link Autor} si ya existe en la BD,
     * o creando uno nuevo en caso contrario. Esto evita duplicados en la tabla de autores.
     */
    private Libro construirLibroConAutorGestionado(LibroRecord record) {
        Libro libro = new Libro(record);

        if (libro.getAutor() != null) {
            Optional<Autor> autorExistente = autorRepository
                    .findByNombreIgnoreCase(libro.getAutor().getNombre());

            autorExistente.ifPresent(libro::setAutor);
        }

        return libro;
    }

    // ─── Consultas al catálogo ────────────────────────────────────────────────

    /**
     * Retorna todos los libros registrados en el catálogo local,
     * ordenados por número de descargas de forma descendente.
     *
     * @return lista de libros ordenada por popularidad
     */
    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAllOrderByDescargasDesc();
    }

    /**
     * Retorna todos los autores registrados en el catálogo,
     * ordenados alfabéticamente.
     *
     * @return lista de autores
     */
    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAllOrderByNombre();
    }

    /**
     * Retorna los autores que estaban vivos en el año indicado.
     *
     * @param anio año de consulta (ej. 1850)
     * @return lista de autores vivos ese año
     */
    public List<Autor> listarAutoresVivosEnAnio(int anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    /**
     * Retorna todos los libros cuyo idioma coincide con el código dado.
     *
     * @param codigoIdioma código ISO 639-1 del idioma (ej. "es", "en")
     * @return lista de libros en ese idioma
     */
    public List<Libro> listarLibrosPorIdioma(String codigoIdioma) {
        Idioma idioma = Idioma.fromCodigo(codigoIdioma);
        if (idioma == Idioma.nd) return List.of();
        return libroRepository.findByIdioma(idioma);
    }

    /**
     * Retorna estadísticas del catálogo: número de libros por idioma.
     *
     * @return lista de arreglos [Idioma, Long] con el conteo por idioma
     */
    public List<Object[]> estadisticasPorIdioma() {
        return libroRepository.countByIdioma();
    }

    /**
     * Retorna el total de libros registrados en el catálogo.
     *
     * @return número total de libros
     */
    public long contarLibros() {
        return libroRepository.count();
    }

    /**
     * Retorna el total de autores registrados en el catálogo.
     *
     * @return número total de autores
     */
    public long contarAutores() {
        return autorRepository.count();
    }
}
