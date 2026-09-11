package com.middleware.apitv.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.middleware.apitv.dto.ShowInfo;
import com.middleware.apitv.dto.TVMazeResponse;
import com.middleware.apitv.service.ShowService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<@Nullable List<TVMazeResponse>> getShows(
            @RequestParam(name = "search_query") String searchQuery) {
        
        @Nullable
		List<TVMazeResponse> results = showService.searchShows(searchQuery);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<@Nullable ShowInfo> getShowById(@PathVariable(name = "id") Long id) {
        @Nullable ShowInfo show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }
}
