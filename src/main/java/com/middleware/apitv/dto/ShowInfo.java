package com.middleware.apitv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShowInfo(
    Long id,
    String name,
    Network network,
    WebChannel webChannel,
    String summary,
    List<String> genres
) {}
