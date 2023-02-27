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

    @ShellMethod(key = "his")
    public void abc(final long chatId) {
        this.clientsAdapter.moveBasketHistory(chatId);
    }

    @ShellMethod(key = "ta")
    public void abcd() {

    }
}
