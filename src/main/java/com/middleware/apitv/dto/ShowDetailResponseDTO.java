package com.middleware.apitv.dto;

import java.util.List;

public record ShowDetailResponseDTO(
    Long id,
    String name,
    String channel,
    String summary,
    List<String> genres,
    List<CommentDTO> comments
) {}
