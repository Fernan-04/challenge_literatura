package com.aluracursos.challenge_literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**

 *
 * @param count    número total de libros que coinciden con la búsqueda
 * @param siguiente URL de la siguiente página (puede ser {@code null})
 * @param anterior  URL de la página anterior (puede ser {@code null})
 * @param resultados lista de libros en la página actual
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RespuestaApiRecord(
        @JsonAlias("count")    Integer count,
        @JsonAlias("next")     String siguiente,
        @JsonAlias("previous") String anterior,
        @JsonAlias("results")  List<LibroRecord> resultados
) {}
