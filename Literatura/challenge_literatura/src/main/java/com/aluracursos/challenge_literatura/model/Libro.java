package com.aluracursos.challenge_literatura.model;

import jakarta.persistence.*;
import java.util.Optional;

/**
 * Entidad JPA que representa un libro en la base de datos
 * El título está restringido a ser único para evitar duplicados en el catálogo.
 */
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Idioma idioma;

    @Column(name = "numero_de_descargas")
    private Integer numeroDeDescargas;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Libro() {}

    /**
     * Construye un {@code Libro} a partir del record JSON deserializado.
     * Toma el primer autor y el primer idioma de las listas provistas.
     *
     * @param record datos crudos del libro provenientes de la API Gutendex
     */
    public Libro(LibroRecord record) {
        this.titulo = record.titulo();

        Optional<AutorRecord> primerAutor = record.autores().stream().findFirst();
        primerAutor.ifPresent(a -> this.autor = new Autor(a));

        Optional<String> primerIdioma = record.idiomas().stream().findFirst();
        primerIdioma.ifPresent(i -> this.idioma = Idioma.fromCodigo(i));

        if (this.idioma == null) this.idioma = Idioma.nd;

        this.numeroDeDescargas = record.numeroDeDescargas() != null ? record.numeroDeDescargas() : 0;
    }

    // ─── Presentación ─────────────────────────────────────────────────────────

    /**
     * Imprime la información del libro en un formato estructurado para consola.
     */
    public void imprimirInformacion() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.printf ("  ║  📖 %-38s║%n",
                truncar(titulo, 37));
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf ("  ║  Autor:      %-28s║%n",
                truncar(autor != null ? autor.getNombre() : "Desconocido", 27));
        System.out.printf ("  ║  Idioma:     %-28s║%n",
                truncar(idioma != null ? idioma.getNombreCompleto() : "Desconocido", 27));
        System.out.printf ("  ║  Descargas:  %-28s║%n",
                String.format("%,d", numeroDeDescargas != null ? numeroDeDescargas : 0));
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    /** Trunca un texto si excede la longitud máxima para alineación en consola. */
    private String truncar(String texto, int max) {
        if (texto == null) return "N/A";
        return texto.length() > max ? texto.substring(0, max - 3) + "..." : texto;
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public Idioma getIdioma() { return idioma; }
    public void setIdioma(Idioma idioma) { this.idioma = idioma; }

    public Integer getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Integer numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }

    @Override
    public String toString() {
        return titulo;
    }
}
