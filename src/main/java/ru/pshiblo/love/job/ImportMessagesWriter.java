package ru.pshiblo.love.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import ru.pshiblo.love.domain.LoveMessage;
import ru.pshiblo.love.repository.LoveMessageRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ImportMessagesWriter implements ItemWriter<LoveMessage> {

    private final LoveMessageRepository repository;

    @Override
    public void write(List<? extends LoveMessage> items) throws Exception {
        log.info("Start write");
        for (LoveMessage message : items) {
            repository.save(message);
        }
    }
}
