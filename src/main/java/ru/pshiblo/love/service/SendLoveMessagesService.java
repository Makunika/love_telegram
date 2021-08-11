package ru.pshiblo.love.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;
import ru.pshiblo.love.repository.LoveMessageRepository;
import ru.pshiblo.love.repository.UserRepository;
import ru.pshiblo.love.telegram.LoveTelegramBot;
import ru.pshiblo.love.telegram.utils.TelegramUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendLoveMessagesService {

    private final UserRepository userRepository;
    private final LoveMessageRepository loveMessageRepository;
    private final LoveTelegramBot bot;

    @Scheduled(cron = "0 0/5 13,18 * * ?")
    //@Scheduled(fixedDelay = 1000)
    public void sendLoveMessages() {
        log.info("Start send");
        List<User> users = userRepository.getAllByState(State.RUN);

        for (User user : users) {
            SendMessage message = TelegramUtils.createMessageTemplate(user);
            message.setText(loveMessageRepository.getRandomMsg().getMessage());
            try {
                bot.execute(message);
            }catch (TelegramApiException e) {
                log.warn("Telegram oops!", e);
            }
        }
    }

}
