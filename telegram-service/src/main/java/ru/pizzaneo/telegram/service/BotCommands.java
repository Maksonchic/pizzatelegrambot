package ru.pizzaneo.telegram.service;

import org.springframework.stereotype.Service;
import ru.pizzaneo.telegram.adapters.ClientsAdapter;
import ru.pizzaneo.telegram.adapters.ProductsAdapter;
import ru.pizzaneo.models.dto.MenuGroupsDto;
import ru.pizzaneo.models.dto.MenuMatrixDto;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BotCommands {

    private final ClientsAdapter clientsAdapter;
    private final ProductsAdapter productsAdapter;

    public BotCommands(ClientsAdapter clientsAdapter, ProductsAdapter productsAdapter) {
        this.clientsAdapter = clientsAdapter;
        this.productsAdapter = productsAdapter;
    }

    public MenuMatrixDto getBasket(final long chatId) {
        try {
            List<String> variationIds = clientsAdapter.getClient(chatId).basketVariationIds();
            if (variationIds.size() > 0) {
                return this.productsAdapter.getProductsListByIds(variationIds);
            } else {
                return new MenuMatrixDto(new ArrayList<>());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public MenuGroupsDto getMenuGroups() throws URISyntaxException {
        return productsAdapter.getProductGroups();
    }

    public MenuMatrixDto getProductsGroupPage(long groupId, int page, int size) throws URISyntaxException {
        return productsAdapter.getProductsListInGroup(groupId, page, size);
    }

    public void addToClientBasket(Long chatId, String variationId) {
        clientsAdapter.saveProductToClient(chatId, variationId);
    }

    public void deleteFromClientBasket(Long chatId, String variationId) {
        clientsAdapter.deleteProductFromClient(chatId, variationId);
    }

    public void clearClientBasket(Long chatId) {
        clientsAdapter.clearClientBasket(chatId);
    }

    public void takeOrder(final long chatId) {
        this.clientsAdapter.moveBasketHistory(chatId);
    }
}
