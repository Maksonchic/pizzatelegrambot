package ru.pizzaneo.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClientDto(
        @JsonProperty("chatId") long chatId
        , @JsonProperty("basket") List<String> basketVariationIds
        , @JsonProperty("lastOrder") List<String> lastOrder
) {
}
