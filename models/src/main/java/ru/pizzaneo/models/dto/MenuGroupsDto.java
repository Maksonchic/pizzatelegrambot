package ru.pizzaneo.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MenuGroupsDto(List<MenuGroupDto> rows) {
    public record MenuGroupDto(
            @JsonProperty("id") long id
            , @JsonProperty("name") String name) {
    }
}
