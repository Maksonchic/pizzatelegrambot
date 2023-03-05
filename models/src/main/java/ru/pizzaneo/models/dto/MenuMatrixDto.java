package ru.pizzaneo.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MenuMatrixDto(List<MenuMatrixRowDto> rows) {

    public void addRowDto(MenuMatrixRowDto item) {
        this.rows.add(item);
    }

    public record MenuMatrixRowDto(
            @JsonProperty("id") String id
            , @JsonProperty("name") String name
            , @JsonProperty("price") double price
    ) {
    }
}
