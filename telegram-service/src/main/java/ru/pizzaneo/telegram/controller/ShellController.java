package ru.pizzaneo.telegram.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.pizzaneo.telegram.adapters.ClientsAdapter;

@ShellComponent
public class ShellController {

    private final ClientsAdapter clientsAdapter;

    public ShellController(ClientsAdapter clientsAdapter) {
        this.clientsAdapter = clientsAdapter;
    }

    @ShellMethod(key = "ap")
    public void addToBasket(long chatId, String variationId) {
        this.clientsAdapter.saveProductToClient(chatId, variationId);
    }

    @ShellMethod(key = "dp")
    public void deleteToBasket(long chatId, String variationId) {
        this.clientsAdapter.deleteProductFromClient(chatId, variationId);
    }

    @ShellMethod(key = "dps")
    public void deleteBasket(long chatId) {
        this.clientsAdapter.clearClientBasket(chatId);
    }
}
