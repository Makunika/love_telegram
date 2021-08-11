package ru.pshiblo.love.job.parser.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.pshiblo.love.job.parser.exceptions.ResponseException;
import ru.pshiblo.love.job.parser.exceptions.ResponseParserException;

import java.net.URI;
import java.util.List;

public abstract class AbstractLoveParser implements LoveParser {

    @Override
    public List<String> getLoveMessages() throws ResponseException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl(), String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseException("Status in response is " + response.getStatusCodeValue());
        }
        if (!response.hasBody()) {
            throw new ResponseException("Response not has body");
        }
        return parse(response.getBody());
    }

    protected abstract URI getUrl();

    protected abstract List<String> parse(String response) throws ResponseParserException;

}
