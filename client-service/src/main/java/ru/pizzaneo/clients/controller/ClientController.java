package ru.pizzaneo.clients.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pizzaneo.clients.domain.Client;
import ru.pizzaneo.clients.service.ClientsService;
import ru.pizzaneo.models.dto.ClientDto;

import java.util.Map;

@RestController
public class ClientController {

    private final ClientsService clientsService;

    public ClientController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @GetMapping(value = "/clients/{chatId}")
    public ClientDto getClient(@PathVariable("chatId") long chatId) {
        return this.clientsService.find(chatId).toDto();
    }

    @PutMapping("/clients/basket/{chatId}")
    public void addProductToBasket(
            @PathVariable("chatId") long chatId
            , @RequestBody String variationId) {
        this.clientsService.addProductToBasket(new Client(chatId, null), variationId);
    }

    @PutMapping("/clients/history/{chatId}")
    public void moveProductBasketToHistory(
            @PathVariable("chatId") long chatId) {
        this.clientsService.moveToHistory(new Client(chatId, null));
    }

    @DeleteMapping("/clients/basket")
    public void removeBasketElement(@RequestBody Map<String, String> data) {
        this.clientsService.deleteFromClientBasket(
                new Client(Long.parseLong(data.get("chatId"))), data.get("productId"));
    }

    @DeleteMapping("/clients/{chatId}/basket")
    public void clearClientBasket(@PathVariable("chatId") long chatId) {
        this.clientsService.clearClientBasket(new Client(chatId, null));
    }
}
