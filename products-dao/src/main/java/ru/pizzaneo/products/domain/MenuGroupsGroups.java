package ru.pizzaneo.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public record MenuGroupsGroups(
        @JsonProperty("id") int id
        , @JsonProperty("name") String name
        , @JsonProperty("path") String path
        , @JsonProperty("productsCount") int productsCount) {
}