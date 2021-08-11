package ru.pshiblo.love.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import ru.pshiblo.love.domain.LoveMessage;

@Slf4j
public class ImportMessagesProcessor implements ItemProcessor<String, LoveMessage> {
    @Override
    public LoveMessage process(String str) throws Exception {
        LoveMessage loveMessage = new LoveMessage();
        loveMessage.setMessage(str);
        return loveMessage;
    }
}
