package com.aluracursos.challenge_literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;


@Service
public class ConvierteDatos {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T convertir(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Error al parsear JSON a " + clase.getSimpleName() + ": " + e.getMessage(), e
            );
        }
    }
}
