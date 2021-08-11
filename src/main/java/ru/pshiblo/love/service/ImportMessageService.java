package ru.pshiblo.love.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pshiblo.love.job.MessagesHolder;
import ru.pshiblo.love.job.parser.base.LoveParser;
import ru.pshiblo.love.job.parser.exceptions.ResponseException;
import ru.pshiblo.love.repository.LoveMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportMessageService {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final MessagesHolder messagesHolder;
    private final LoveMessageRepository repository;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Moscow") // Формат:  секунда, минута, час, день, месяц, день недели
    public void importMessages() throws ResponseException {
        log.info("Start import messages");
        repository.deleteAll();
        messagesHolder.update();
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(job, params);
            log.info("Finish import messages with status " + execution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Import messages not completed", e);
        }
    }

}
