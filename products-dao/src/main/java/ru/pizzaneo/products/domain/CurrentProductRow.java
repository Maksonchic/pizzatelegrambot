package ru.pizzaneo.products.domain;

import ru.pizzaneo.models.dto.MenuMatrixDto;

import java.util.ArrayList;
import java.util.List;

public record CurrentProductRow(
        String id
        , String name
        , List<Variation> variations) {
    record Variation(Price price) {
        record Price(double value) {
        }
    }

    public MenuMatrixDto.MenuMatrixRowDto toDto() {
        return new MenuMatrixDto.MenuMatrixRowDto(this.id(), this.name(), this.variations().get(0).price().value());
    }

    public static MenuMatrixDto toDto(final List<CurrentProductRow> rows) {
        MenuMatrixDto dtos = new MenuMatrixDto(new ArrayList<>());
        for (CurrentProductRow row : rows) {
            dtos.addRowDto(row.toDto());
        }
        return dtos;
    }
}
