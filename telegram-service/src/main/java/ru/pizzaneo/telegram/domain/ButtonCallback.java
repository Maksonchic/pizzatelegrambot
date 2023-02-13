package ru.pizzaneo.telegram.domain;

import ru.pizzaneo.telegram.controller.BotEndpoint;

public class ButtonCallback {

    private final String command;
    private final String id;
    private final int currentPageNum;

    public ButtonCallback(String command, String id, int currentPageNum) {
        this.command = command;
        this.id = id;
        this.currentPageNum = currentPageNum;
    }

    public static ButtonCallback queryToButtonCallback(final String callbackQueryData) {
        String[] strs = callbackQueryData.split("-", 2);
        String command = strs[0];
        if (command.equals(BotEndpoint.eBtnCommandsPrefix.menu.name())) {
            strs = callbackQueryData.split("-", 3);
            return new ButtonCallback(command, strs[1], Integer.parseInt(strs[2]));
        } else {
            return new ButtonCallback(command, strs[1], 0);
        }
    }

    public String getCommand() {
        return command;
    }

    public String getId() {
        return id;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }
}
