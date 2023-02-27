package ru.pizzaneo.clients.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pizzaneo.clients.domain.Client;
import ru.pizzaneo.clients.repository.BasketDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientsServiceImpl implements ClientsService {

    private final BasketDao basketRepository;

    public ClientsServiceImpl(BasketDao basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Client find(long chatId) {
        Client client = new Client(chatId);
        client.setBasketVariationIds(this.basketRepository.findByChatId(chatId));
        client.setLastOrder(this.basketRepository.findHistoryByChatId(chatId));
        return client;
    }

    @Override
    @Transactional
    public void clearClientBasket(Client client) {
        this.basketRepository.saveClientBasket(new Client(client.getChatId(), new ArrayList<>()));
    }

    @Override
    @Transactional
    public void deleteFromClientBasket(Client client, String variationId) {
        List<String> clientBasket = this.basketRepository.findByChatId(client.getChatId());
        // need to remove first element only
        //noinspection RedundantCollectionOperation
        clientBasket.remove(clientBasket.indexOf(variationId));
        this.basketRepository.saveClientBasket(new Client(client.getChatId(), clientBasket));
    }

    @Override
    @Transactional
    public void moveToHistory(Client client) {
        this.basketRepository.updateClientHistory(client);
        this.basketRepository.saveClientBasket(new Client(client.getChatId(), new ArrayList<>()));
    }

    @Override
    @Transactional
    public void addProductToBasket(Client client, String variationId) {
        List<String> clientBasket = this.basketRepository.findByChatId(client.getChatId());
        clientBasket.add(variationId);
        this.basketRepository.saveClientBasket(new Client(client.getChatId(), clientBasket));
    }
}
