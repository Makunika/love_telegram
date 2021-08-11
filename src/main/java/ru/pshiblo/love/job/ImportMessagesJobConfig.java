package ru.pshiblo.love.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pshiblo.love.domain.LoveMessage;
import ru.pshiblo.love.repository.LoveMessageRepository;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ImportMessagesJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MessagesHolder messagesHolder;

    @Bean
    public ImportMessagesReader reader() {
        return new ImportMessagesReader(messagesHolder);
    }

    @Bean
    public ImportMessagesProcessor processor() {
        return new ImportMessagesProcessor();
    }

    @Bean
    public ImportMessagesWriter writer(LoveMessageRepository repository) {
        return new ImportMessagesWriter(repository);
    }

    @Bean
    public Job importMessagesJob(Step step1) {
        return jobBuilderFactory.get("importMessagesJob")
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ImportMessagesWriter writer) {
        return stepBuilderFactory.get("step1")
                .<String, LoveMessage>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
