package com.middleware.apitv.dto;

public record AnalisisRequestDTO(
    Long show_id,
    String comment,
    int rating
) {}
