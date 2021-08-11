package ru.pshiblo.love.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;
import ru.pshiblo.love.repository.UserRepository;
import ru.pshiblo.love.telegram.utils.TelegramUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StopHandler implements Handler {

    public final static String RUN = "/run";

    private final UserRepository repository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.startsWith(RUN)) {
            user.setState(State.RUN);
            repository.save(user);
            SendMessage msg = TelegramUtils.createMessageTemplate(user);
            msg.setText("Еййй, комплименты опять приходят!");
            return Collections.singletonList(msg);
        }
        return Collections.emptyList();
    }

    @Override
    public State getHandlerState() {
        return State.STOP;
    }
}
