package ru.pshiblo.love.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;
import ru.pshiblo.love.repository.UserRepository;
import ru.pshiblo.love.telegram.handler.Handler;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class LoveTelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;

    private final List<Handler> handlers;
    private final UserRepository userRepository;

    public LoveTelegramBot(List<Handler> handlers, UserRepository userRepository) {
        this.handlers = handlers;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = null;

        if (isMessageWithText(update)) {
            Message message = update.getMessage();
            long telegramId = message.getFrom().getId();
            User user = getUser(telegramId, message.getFrom().getUserName());
            messagesToSend = getHandlerByState(user.getState()).handle(user, message.getText());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            long telegramId = callbackQuery.getFrom().getId();
            User user = getUser(telegramId, callbackQuery.getFrom().getUserName());
            messagesToSend = getHandlerByState(user.getState()).handle(user, callbackQuery.getData());
        }

        if (messagesToSend == null) {
            log.warn("Not supported message");
            throw new UnsupportedOperationException();
        }

        messagesToSend.forEach(response -> {
            if (response instanceof SendMessage) {
                try {
                    execute((SendMessage) response);
                }catch (TelegramApiException e) {
                    log.error("Telegram oops", e);
                }
            }
        });
    }

    private User getUser(long telegramId, String username) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setTelegramId(telegramId);
                    newUser.setUsername(username);
                    return userRepository.save(newUser);
                });
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(h -> h.getHandlerState() == state)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("State " + state.name() + " in handlers not found"));
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
