package com.middleware.apitv.service;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.middleware.apitv.dto.AnalisisRequestDTO;
import com.middleware.apitv.dto.AnalisisShow;
import com.middleware.apitv.dto.CommentDTO;
import com.middleware.apitv.dto.ShowDetailResponseDTO;
import com.middleware.apitv.dto.ShowInfo;
import com.middleware.apitv.dto.ShowResponseDTO;
import com.middleware.apitv.dto.TVMazeResponse;
import com.middleware.apitv.repository.AnalisisShowRepository;
import com.middleware.apitv.repository.ShowRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowService {

	private final RestClient restClient;
	private final ShowRepository showRepository;
	private final AnalisisShowRepository analisisShowRepository;
	
    public ShowService(ShowRepository showRepository, AnalisisShowRepository analisisShowRepository,
    		@Value("${api.shows}") String apiExternaShows) {
    	this.analisisShowRepository = analisisShowRepository;
    	this.showRepository = showRepository;
        this.restClient = RestClient.builder()
                .baseUrl(apiExternaShows)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public List<ShowResponseDTO> searchShows(String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            return Collections.emptyList();
        }

        try {
            List<TVMazeResponse> externalData = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/shows")
                            .queryParam("q", searchQuery)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TVMazeResponse>>() {});

            if (externalData == null) {
                return Collections.emptyList();
            }

            return externalData.stream()
                    .map(TVMazeResponse::show)
                    .map(show -> {
                        String channelName = null;
                        if (show.network() != null) {
                            channelName = show.network().name();
                        } else if (show.webChannel() != null) {
                            channelName = show.webChannel().name();
                        }

                        List<CommentDTO> dbComments = analisisShowRepository.findByShowId(show.id())
                                .stream()
                                .map(analisis -> new CommentDTO(analisis.getComment(), analisis.getRating()))
                                .collect(Collectors.toList());

                        
                        return new ShowResponseDTO(
                                show.id(),
                                show.name(),
                                channelName,
                                show.summary(),
                                show.genres(),
                                dbComments 
                        );
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error al buscar shows y comentarios: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public ShowDetailResponseDTO getShowById(Long id) {
        if (id == null) {
            return null;
        }

        ShowInfo showBase = null;

        Optional<ShowInfo> localShow = showRepository.findById(id);
        if (localShow.isPresent()) {
            System.out.println("Show encontrado en MongoDB (Local).");
            showBase = localShow.get();
        } else {
            System.out.println("Show no encontrado en Mongo. Consultando API externa de TVMaze para ID: " + id);
            try {
                ShowInfo externalShow = restClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/shows/{show_id}").build(id))
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (request, response) -> {
                            throw new RuntimeException("TVMaze rechazó la solicitud con código: " + response.getStatusCode());
                        })
                        .body(ShowInfo.class);

                if (externalShow != null) {
                    showRepository.save(externalShow);
                    System.out.println("Show guardado exitosamente en MongoDB Atlas.");
                    showBase = externalShow;
                }
            } catch (Exception e) {
                System.err.println("Error controlado en el flujo por ID del Middleware: " + e.getMessage());
                return null;
            }
        }

        if (showBase == null) {
            return null;
        }

        String channelName = null;
        if (showBase.network() != null) {
            channelName = showBase.network().name();
        } else if (showBase.webChannel() != null) {
            channelName = showBase.webChannel().name();
        }

        List<CommentDTO> dbComments = analisisShowRepository.findByShowId(id)
                .stream()
                .map(analisis -> new CommentDTO(analisis.getComment(), analisis.getRating()))
                .collect(Collectors.toList());

        return new ShowDetailResponseDTO(
                showBase.id(),
                showBase.name(),
                channelName,
                showBase.summary(),
                showBase.genres(),
                dbComments
        );
    }
    
    public void guardarAnalisis(AnalisisRequestDTO request) {
        if (request.rating() < 0 || request.rating() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5.");
        }

         ShowDetailResponseDTO show = this.getShowById(request.show_id());
        if (show == null) {
            throw new RuntimeException("No se puede guardar el análisis. El show con ID " + request.show_id() + " no existe.");
        }

        AnalisisShow analisis = new AnalisisShow(
                request.show_id(),
                request.comment(),
                request.rating()
        );

        analisisShowRepository.save(analisis);
        System.out.println("Análisis guardado con éxito en MongoDB para el show ID: " + request.show_id());
    }
    
}