package com.middleware.apitv.dto;

import java.util.List;

public record ShowResponseDTO(
    Long id,
    String name,
    String channel,
    String summary,
    List<String> genres
) {}
