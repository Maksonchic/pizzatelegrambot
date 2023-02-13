package ru.pizzaneo.products.service;

import ru.pizzaneo.products.domain.CurrentProductRow;
import ru.pizzaneo.products.domain.MenuGroups;
import ru.pizzaneo.products.domain.MenuMatrix;
import ru.pizzaneo.products.domain.ProductRow;

import java.util.List;

public interface ProductService {
    MenuGroups getMenuGroups();
    MenuMatrix getMenuMatrix(long groupId, int page, int size);
    CurrentProductRow getCurrentProductInfo(final String productId);
    List<CurrentProductRow> getProductsInfo(List<String> productIds);
}
