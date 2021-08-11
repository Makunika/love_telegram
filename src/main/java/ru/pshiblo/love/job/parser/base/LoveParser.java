package ru.pshiblo.love.job.parser.base;

import ru.pshiblo.love.job.parser.exceptions.ResponseException;
import ru.pshiblo.love.job.parser.exceptions.ResponseParserException;

import java.util.List;

public interface LoveParser {
    List<String> getLoveMessages() throws ResponseException;
}
