package ru.pizzaneo.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRowDto(
        @JsonProperty("id") String id
        , @JsonProperty("name") String name
        , @JsonProperty("price") double price
) {
}
