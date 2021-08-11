package ru.pshiblo.love.job;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pshiblo.love.job.parser.base.LoveParser;
import ru.pshiblo.love.job.parser.exceptions.ResponseException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class MessagesHolder {
    private final List<LoveParser> parserList;

    private List<String> messages;

    @PostConstruct
    public void update() throws ResponseException {
        messages = new ArrayList<>();
        for (LoveParser loveParser : parserList) {
            messages.addAll(loveParser.getLoveMessages());
        }
    }
}
