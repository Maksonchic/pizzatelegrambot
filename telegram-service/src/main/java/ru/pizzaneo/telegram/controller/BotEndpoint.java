package ru.pizzaneo.telegram.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.pizzaneo.telegram.domain.ButtonCallback;
import ru.pizzaneo.telegram.service.BotCommands;
import ru.pizzaneo.models.dto.MenuMatrixDto;
import ru.pizzaneo.telegram.support.BotButtonsBuilder;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotEndpoint extends TelegramLongPollingBot {

    public enum eBtnCommandsPrefix {basket_rem, basket_add, menu}

    private final String botName;
    private final String token;
    private final BotCommands botController;
    private final BotButtonsBuilder buttonsBuilder;
    private final int menuPageSize;

    public BotEndpoint(
            @Value("${bot.name}") String botName
            , @Value("${bot.token}") String token
            , @Value("${config.buttons-size}") int menuPageSize
            , BotCommands botController, BotButtonsBuilder buttonsBuilder) {
        this.botName = botName;
        this.token = token;
        this.botController = botController;
        this.menuPageSize = menuPageSize;
        this.buttonsBuilder = buttonsBuilder;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            List<InlineKeyboardButton> buttons;
            if (update.getCallbackQuery() != null) {
                buttons = this.processBtnCallBack(update.getCallbackQuery());
                this.answer(update.getCallbackQuery().getMessage().getChatId(), "Блюдо", buttons);
            } else {
                if (update.getMessage().getText().equals("/start")) {
                    this.greetingsNewUser(update.getMessage());
                    return;
                } else if (update.getMessage().getText().charAt(0) == '/') {
                    buttons = this.processCommandMessage(update.getMessage().getText(), update.getMessage().getChatId());
                } else {
                    buttons = this.processStringMessage(update.getMessage().getText());
                }
                this.answer(update.getMessage().getChatId(), "Раздел", buttons);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private List<InlineKeyboardButton> processStringMessage(String message) {
        return List.of(InlineKeyboardButton.builder().text(message).callbackData("to-menu").build());
    }

    private List<InlineKeyboardButton> processCommandMessage(
            String message, long chatId) throws URISyntaxException {
        switch (message.substring(1)) {
            case "menu":
                return botController.getMenuGroups().rows()
                        .stream().map(b ->
                                InlineKeyboardButton.builder().text(b.name()).callbackData(
                                        eBtnCommandsPrefix.menu.name() + "-" + b.id() + "-0").build())
                        .collect(Collectors.toList());
            case "basket":
                return botController.getBasket(chatId).rows().stream().map(b ->
                                InlineKeyboardButton.builder().text(b.name()).callbackData(
                                        eBtnCommandsPrefix.basket_rem.name() + "-" + b.id()).build())
                        .collect(Collectors.toList());
            default:
                return null;
        }
    }

    private List<InlineKeyboardButton> processBtnCallBack(CallbackQuery callbackQuery) throws URISyntaxException {
        ButtonCallback callback = ButtonCallback.queryToButtonCallback(callbackQuery.getData());
        if (callback.getCommand().equals(eBtnCommandsPrefix.menu.name())) {
            MenuMatrixDto rows = botController
                    .getProductsGroupPage(
                            Integer.parseInt(callback.getId())
                            , callback.getCurrentPageNum()
                            , this.menuPageSize);
            return buttonsBuilder.createProductsPageButtons(rows, callback);
        } else if (callback.getCommand().equals(eBtnCommandsPrefix.basket_add.name())) {
            this.botController.addToClientBasket(callbackQuery.getMessage().getChatId(), callback.getId());
            return botController.getMenuGroups().rows()
                    .stream().map(b ->
                            InlineKeyboardButton.builder().text(b.name()).callbackData(
                                    eBtnCommandsPrefix.menu.name() + "-" + b.id() + "-0").build())
                    .collect(Collectors.toList());
        } else if (callback.getCommand().equals(eBtnCommandsPrefix.basket_rem.name())) {
            this.botController.deleteFromClientBasket(callbackQuery.getMessage().getChatId(), callback.getId());
            return botController.getBasket(callbackQuery.getMessage().getChatId()).rows().stream().map(b ->
                            InlineKeyboardButton.builder().text(b.name()).callbackData(
                                    eBtnCommandsPrefix.basket_rem.name() + "-" + b.id()).build())
                    .collect(Collectors.toList());
        }
        return null;
    }

    private void answer(long chatId, String text, List<InlineKeyboardButton> btns) {
        try {
            SendMessage answer = new SendMessage();
            answer.setText(text);
            answer.setReplyMarkup(createButtonsGrid(btns));
            answer.setChatId(chatId);
            this.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void greetingsNewUser(final Message message) {
        try {
            SendMessage answer = new SendMessage();
            answer.setText("Привет!\r\nДля начала работы нажми команду /menu\r\nКорзину можно посмотреть здесь /basket");
            answer.setChatId(message.getChatId());
            this.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private InlineKeyboardMarkup createButtonsGrid(List<InlineKeyboardButton> btns) {
        final int rowSize = 2;
        List<List<InlineKeyboardButton>> mList = new ArrayList<>();
        List<InlineKeyboardButton> line = null;
        for (int index = 0; index < btns.size(); index++) {
            if (index % rowSize == 0) {
                line = new ArrayList<>();
            }
            line.add(btns.get(index));
            if (index % rowSize == (rowSize - 1) || btns.size() == (index + 1)) {
                mList.add(line);
            }
        }
        return new InlineKeyboardMarkup(mList);
    }
}
