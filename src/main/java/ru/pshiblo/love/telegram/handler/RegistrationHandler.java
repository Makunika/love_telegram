package ru.pshiblo.love.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;
import ru.pshiblo.love.repository.UserRepository;
import ru.pshiblo.love.telegram.utils.TelegramUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.pshiblo.love.telegram.handler.RunHandler.GET_CON;

@Component
@RequiredArgsConstructor
public class RegistrationHandler implements Handler {

    public final static String START_BOT = "/start_bot";
    private final UserRepository repository;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.startsWith(START_BOT)) {
            user.setState(State.RUN);
            repository.save(user);
            SendMessage msg = TelegramUtils.createMessageTemplate(user);
            msg.setText("Оууууу! Комплименты будут приходить автоматически, ня");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttons = Collections.singletonList(TelegramUtils.createInlineKeyboardButton("Получить комплимент", GET_CON));
            inlineKeyboardMarkup.setKeyboard(Collections.singletonList(buttons));
            msg.setReplyMarkup(inlineKeyboardMarkup);
            return Collections.singletonList(msg);
        } else {
            SendMessage helloMessage = TelegramUtils.createMessageTemplate(user);
            helloMessage.setText("Самые милые комплименты любимой девушке Насте :3");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttons = Collections.singletonList(TelegramUtils.createInlineKeyboardButton("Начать", START_BOT));
            inlineKeyboardMarkup.setKeyboard(Collections.singletonList(buttons));
            helloMessage.setReplyMarkup(inlineKeyboardMarkup);
            return Collections.singletonList(helloMessage);
        }
    }

    @Override
    public State getHandlerState() {
        return State.REGISTRATION;
    }
}
