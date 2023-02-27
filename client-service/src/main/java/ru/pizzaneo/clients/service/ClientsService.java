package ru.pizzaneo.clients.service;

import ru.pizzaneo.clients.domain.Client;

public interface ClientsService {
    Client find(long chatId);
    void addProductToBasket(final Client client, final String variationId);
    void clearClientBasket(final Client client);
    void deleteFromClientBasket(final Client client,final  String productId);
    void moveToHistory(Client client);
}
