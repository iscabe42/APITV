package com.middleware.apitv.service;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.middleware.apitv.dto.ShowInfo;
import com.middleware.apitv.dto.TVMazeResponse;

import java.util.Collections;
import java.util.List;

@Service
public class ShowService {

	private final RestClient restClient;

    public ShowService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.tvmaze.com")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public @Nullable List<TVMazeResponse> searchShows(String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/shows")
                            .queryParam("q", searchQuery)
                            .build())
                    .retrieve()
                    // Si el servidor responde con errores 4xx o 5xx, capturamos el cuerpo crudo (HTML)
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String errorHtml = new String(response.getBody().readAllBytes());
                        System.err.println("------ ERROR DE LA API EXTERNA ------");
                        System.err.println("Código de Estado: " + response.getStatusCode());
                        System.err.println("Respuesta del Servidor (HTML): \n" + errorHtml);
                        System.err.println("-------------------------------------");
                        throw new RuntimeException("TVMaze respondió con un error y formato no soportado.");
                    })
                    // Si todo sale bien (200 OK), procedemos con el parseo seguro
                    .body(new ParameterizedTypeReference<List<TVMazeResponse>>() {});

        } catch (Exception e) {
            System.err.println("Error controlado en el Middleware: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public ShowInfo getShowById(Long id) {
        if (id == null) {
            return null;
        }

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/shows/{show_id}")
                            .build(id)) // Reemplaza {show_id} con el parámetro 'id'
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String errorHtml = new String(response.getBody().readAllBytes());
                        System.err.println("Error buscando show ID " + id + ": " + response.getStatusCode());
                        throw new RuntimeException("No se pudo obtener el show de TVMaze");
                    })
                    .body(ShowInfo.class); // Mapea directamente al DTO del show completo

        } catch (Exception e) {
            System.err.println("Error en el Middleware al buscar por ID: " + e.getMessage());
            return null;
        }
    }
}