package ru.pshiblo.love.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    State getHandlerState();
}
