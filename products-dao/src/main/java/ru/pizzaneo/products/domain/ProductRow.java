package ru.pizzaneo.products.domain;

import java.util.List;

public record ProductRow(
        String id
        , String name
        , String productCode
        , List<MenuMatrixVariation> variations
        , List<Long> groups) {
    public record MenuMatrixVariation(String id, int price, MenuProduct menuProduct) {
        public record MenuProduct(String id) {
        }
    }
}
