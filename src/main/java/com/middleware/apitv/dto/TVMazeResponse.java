package com.middleware.apitv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TVMazeResponse(double score, ShowInfo show) {}
