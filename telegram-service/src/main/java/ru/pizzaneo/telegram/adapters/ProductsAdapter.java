package ru.pizzaneo.telegram.adapters;

import ru.pizzaneo.models.dto.MenuGroupsDto;
import ru.pizzaneo.models.dto.MenuMatrixDto;

import java.net.URISyntaxException;
import java.util.List;

public interface ProductsAdapter {
    MenuGroupsDto getProductGroups() throws URISyntaxException;
    MenuMatrixDto getProductsListInGroup(long groupId, int page, int size) throws URISyntaxException;
    MenuMatrixDto getProductsListByIds(List<String> variationIds) throws URISyntaxException;
}
