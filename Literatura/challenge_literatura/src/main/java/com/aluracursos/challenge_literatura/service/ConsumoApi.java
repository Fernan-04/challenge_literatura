package com.aluracursos.challenge_literatura.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoApi {

    private final String URL_BASE="https://gutendex.com/books";

    public String obtenerDatos(String titulo){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "/?search=" + titulo.replace(" ","+")))
                .build();
        HttpResponse<String> response = null;

        try {
            HttpResponse<String> respuesta = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (respuesta.statusCode() != 200) {
                throw new RuntimeException(
                        "Error en la API. Código HTTP: " + respuesta.statusCode()
                );
            }

            return respuesta.body();

        } catch (IOException e) {
            throw new RuntimeException("Error de conexión con la API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La petición fue interrumpida: " + e.getMessage(), e);
        }
    }
}
