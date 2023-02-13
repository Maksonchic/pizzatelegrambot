package ru.pizzaneo.clients.repository;

import ru.pizzaneo.clients.domain.Client;

import java.util.List;

public interface BasketDao {
    List<String> findByChatId(final long chatId);
    List<String> findHistoryByChatId(long chatId);
    void saveClientBasket(final Client client);
}
