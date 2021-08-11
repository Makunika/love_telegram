package ru.pshiblo.love.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ImportMessagesReader implements ItemReader<String> {

    private final MessagesHolder messages;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Start read");
        if (!messages.getMessages().isEmpty()) {
            return messages.getMessages().remove(0);
        }
        return null;
    }
}
