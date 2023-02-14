package ru.pizzaneo.telegram.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.pizzaneo.models.dto.MenuMatrixDto;
import ru.pizzaneo.telegram.controller.BotEndpoint;
import ru.pizzaneo.telegram.domain.ButtonCallback;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BotButtonsBuilder {

    private final int menuPageSize;

    public BotButtonsBuilder(@Value("${config.buttons-size}") int menuPageSize) {
        this.menuPageSize = menuPageSize;
    }

    public List<InlineKeyboardButton> createProductsPageButtons(final MenuMatrixDto menuItems, ButtonCallback callback) {
        List<InlineKeyboardButton> buttons = menuItems.rows().stream().map(b -> {
                    String name = b.name();
                    String callbackData = BotEndpoint.eBtnCommandsPrefix.basket_add.name() + "-" + b.id();
                    return InlineKeyboardButton
                            .builder()
                            .text(name)
                            .callbackData(callbackData)
                            .build();
                })
                .collect(Collectors.toList());
        if (callback.getCurrentPageNum() > 0 || buttons.size() == this.menuPageSize) {

            if (callback.getCurrentPageNum() > 0) {
                buttons.add(InlineKeyboardButton.builder()
                        .text("Назад")
                        .callbackData(BotEndpoint.eBtnCommandsPrefix.menu.name()
                                + "-" + callback.getId()
                                + "-" + (callback.getCurrentPageNum() - 1)).build());

            }
            if (buttons.size() == this.menuPageSize) {
                buttons.add(InlineKeyboardButton.builder()
                        .text("Показать ещё")
                        .callbackData(BotEndpoint.eBtnCommandsPrefix.menu.name()
                                + "-" + callback.getId()
                                + "-" + (callback.getCurrentPageNum() + 1)).build());
            }
        }
        return buttons;
    }
}
