package com.middleware.apitv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "show") 
@JsonIgnoreProperties(ignoreUnknown = true)
public record ShowInfo(
	@Id  Long id,
    String name,
    Network network,
    WebChannel webChannel,
    String summary,
    List<String> genres
) {}
