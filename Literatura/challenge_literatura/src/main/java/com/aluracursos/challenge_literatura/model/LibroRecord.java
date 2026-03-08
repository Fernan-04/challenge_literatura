package com.aluracursos.challenge_literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**

 *
 * @param id               identificador de Gutenberg
 * @param titulo           título del libro
 * @param autores          lista de autores (se usa el primero como autor principal)
 * @param idiomas          lista de códigos de idioma ISO 639-1
 * @param numeroDeDescargas cantidad de descargas registradas en Gutenberg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroRecord(
        @JsonAlias("id")             Long id,
        @JsonAlias("title")          String titulo,
        @JsonAlias("authors")        List<AutorRecord> autores,
        @JsonAlias("languages")      List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas
) {}
