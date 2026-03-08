package com.aluracursos.challenge_literatura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa a un autor en la base de datos.
 * Tiene una relación OneToMany con {@link Libro}.
 */
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "anio_nacimiento")
    private Integer anioNacimiento;

    @Column(name = "anio_fallecimiento")
    private Integer anioFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    // ─── Constructores ────────────────────────────────────────────────────────

    public Autor() {}

    /**
     * Construye un {@code Autor} a partir del record JSON deserializado.
     *
     * @param record datos crudos provenientes de la API Gutendex
     */
    public Autor(AutorRecord record) {
        this.nombre           = record.nombre();
        this.anioNacimiento   = record.anioNacimiento();
        this.anioFallecimiento = record.anioFallecimiento();
    }

    // ─── Lógica de negocio ────────────────────────────────────────────────────

    /**
     * Determina si el autor estaba vivo en el año proporcionado.
     * Un autor está vivo en un año si nació antes o durante ese año
     * y aún no había fallecido (o se desconoce el año de fallecimiento).
     *
     * @param anio año de consulta
     * @return {@code true} si el autor estaba vivo ese año
     */
    public boolean estabaVivoEn(int anio) {
        boolean yaHabiaNacido   = (anioNacimiento == null) || (anioNacimiento <= anio);
        boolean noHabiaFallecido = (anioFallecimiento == null) || (anioFallecimiento >= anio);
        return yaHabiaNacido && noHabiaFallecido;
    }

    /**
     * Imprime la información del autor en un formato legible por consola.
     */
    public void imprimirInformacion() {
        System.out.println("  ┌─────────────────────────────────────────");
        System.out.printf ("  │ Autor:       %s%n", nombre);
        System.out.printf ("  │ Nacimiento:  %s%n",
                anioNacimiento != null ? anioNacimiento : "Desconocido");
        System.out.printf ("  │ Fallecimiento: %s%n",
                anioFallecimiento != null ? anioFallecimiento : "Desconocido / Aún vivo");

        if (!libros.isEmpty()) {
            System.out.print("  │ Libros:      ");
            System.out.println(libros.stream()
                    .map(Libro::getTitulo)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Sin libros registrados"));
        }
        System.out.println("  └─────────────────────────────────────────");
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAnioNacimiento() { return anioNacimiento; }
    public void setAnioNacimiento(Integer anioNacimiento) { this.anioNacimiento = anioNacimiento; }

    public Integer getAnioFallecimiento() { return anioFallecimiento; }
    public void setAnioFallecimiento(Integer anioFallecimiento) { this.anioFallecimiento = anioFallecimiento; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString() {
        return nombre;
    }
}
