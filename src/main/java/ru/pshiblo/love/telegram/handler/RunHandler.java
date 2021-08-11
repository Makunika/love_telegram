package ru.pshiblo.love.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;
import ru.pshiblo.love.repository.LoveMessageRepository;
import ru.pshiblo.love.repository.UserRepository;
import ru.pshiblo.love.telegram.utils.TelegramUtils;

import java.io.Serializable;
import java.util.List;

import static ru.pshiblo.love.telegram.handler.StopHandler.RUN;

@Component
@RequiredArgsConstructor
public class RunHandler implements Handler {

    public final static String GET_CON = "/compliment";
    public final static String STOP = "/stop";

    private final LoveMessageRepository loveMessageRepository;
    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.startsWith(GET_CON)) {
            SendMessage msg = TelegramUtils.createMessageTemplate(user);
            msg.setText(loveMessageRepository.getRandomMsg().getMessage());
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttons = List.of(TelegramUtils.createInlineKeyboardButton("Еще комплимент!", GET_CON));
            inlineKeyboardMarkup.setKeyboard(List.of(buttons));
            msg.setReplyMarkup(inlineKeyboardMarkup);
            return List.of(msg);
        } else if (message.startsWith(STOP)) {
            user.setState(State.STOP);
            userRepository.save(user);
            SendMessage msg = TelegramUtils.createMessageTemplate(user);
            msg.setText("Теперь комплименты не будут приходить :с Если хочешь их опять получать введи /run");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttons = List.of(TelegramUtils.createInlineKeyboardButton("Хочу снова получать комплименты", RUN));
            inlineKeyboardMarkup.setKeyboard(List.of(buttons));
            msg.setReplyMarkup(inlineKeyboardMarkup);
            return List.of(msg);
        }
        return List.of();
    }

    @Override
    public State getHandlerState() {
        return State.RUN;
    }
}
