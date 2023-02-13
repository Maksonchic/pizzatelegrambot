package ru.pizzaneo.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.pizzaneo.models.dto.MenuMatrixDto;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
public record MenuMatrix(List<ProductRow> rows) {
    public MenuMatrixDto toDto() {
        return new MenuMatrixDto(this.rows().stream().map(row ->
                        new MenuMatrixDto.MenuMatrixRowDto(
                                row.variations().get(0).menuProduct().id()
                                , row.name()
                                , row.variations().get(0).price()))
                .collect(Collectors.toList()));
    }
}

