package ru.pizzaneo.products.dao;

import ru.pizzaneo.products.domain.CurrentProductRow;
import ru.pizzaneo.products.domain.MenuGroups;
import ru.pizzaneo.products.domain.MenuMatrix;
import ru.pizzaneo.products.domain.ProductRow;

import java.util.List;

public interface ProductDao {
    MenuGroups requestMenuGroups(String ticket);
    MenuMatrix requestMenuMatrix(String ticket, long groupId, int page, int size);
    CurrentProductRow requestProductRow(String ticket, String productId);

    List<CurrentProductRow> requestProductsRows(String ticket, List<String> productIds);
}
