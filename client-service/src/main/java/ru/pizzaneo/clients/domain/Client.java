package ru.pizzaneo.clients.domain;

import ru.pizzaneo.models.dto.ClientDto;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private final long chatId;
    private List<String> basketVariationIds;
    private List<String> lastOrder;

    public Client(long chatId) {
        this.chatId = chatId;
        this.basketVariationIds = new ArrayList<>();
        this.lastOrder = new ArrayList<>();
    }

    public Client(long chatId, List<String> basketVariationIds) {
        this.chatId = chatId;
        this.basketVariationIds = basketVariationIds;
        this.lastOrder = new ArrayList<>();
    }

    public Client(long chatId, List<String> basketVariationIds, List<String> lastOrder) {
        this.chatId = chatId;
        this.basketVariationIds = basketVariationIds;
        this.lastOrder = lastOrder;
    }

    public ClientDto toDto() {
        return new ClientDto(this.chatId, this.basketVariationIds, this.lastOrder);
    }

    public static Client fromDto(final ClientDto clientDto) {
        return new Client(clientDto.chatId(), clientDto.basketVariationIds(), clientDto.lastOrder());
    }

    public long getChatId() {
        return chatId;
    }

    public List<String> getBasketVariationIds() {
        return basketVariationIds;
    }

    public List<String> getLastOrderVariationIds() {
        return lastOrder;
    }

    public void setBasketVariationIds(List<String> basketVariationIds) {
        this.basketVariationIds = basketVariationIds;
    }

    public void setLastOrder(List<String> lastOrder) {
        this.lastOrder = lastOrder;
    }
}
