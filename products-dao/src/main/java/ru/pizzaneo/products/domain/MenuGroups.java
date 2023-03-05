package ru.pizzaneo.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.pizzaneo.models.dto.MenuGroupsDto;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
public record MenuGroups(@JsonProperty("groups") List<MenuGroupsGroups> groups) {
    public MenuGroupsDto toDto() {
        return new MenuGroupsDto(this.groups().stream()
                .map(group -> new MenuGroupsDto.MenuGroupDto(group.id(), group.name()))
                .collect(Collectors.toList()));
    }
}
