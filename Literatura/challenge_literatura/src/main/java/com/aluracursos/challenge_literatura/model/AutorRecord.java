package com.aluracursos.challenge_literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @param nombre           nombre completo del autor (formato: "Apellido, Nombre")
 * @param anioNacimiento   año de nacimiento; puede ser {@code null}
 * @param anioFallecimiento año de fallecimiento; puede ser {@code null}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorRecord(
        @JsonAlias("name")       String nombre,
        @JsonAlias("birth_year") Integer anioNacimiento,
        @JsonAlias("death_year") Integer anioFallecimiento
) {}
