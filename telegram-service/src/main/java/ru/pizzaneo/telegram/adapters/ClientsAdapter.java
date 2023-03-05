package ru.pizzaneo.telegram.adapters;

import ru.pizzaneo.models.dto.ClientDto;

import java.net.URISyntaxException;

public interface ClientsAdapter {
    ClientDto getClient(long chatId) throws URISyntaxException;
    void saveProductToClient(long chatId, String variationId);
    void deleteProductFromClient(long chatId, String variationId);
    void clearClientBasket(long chatId);
    void moveBasketHistory(long chatId);
}
