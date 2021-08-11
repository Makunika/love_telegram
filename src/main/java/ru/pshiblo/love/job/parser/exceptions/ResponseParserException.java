package ru.pshiblo.love.job.parser.exceptions;

public class ResponseParserException extends ResponseException {
    public ResponseParserException() {
        super();
    }

    public ResponseParserException(String message) {
        super(message);
    }

    public ResponseParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseParserException(Throwable cause) {
        super(cause);
    }

    protected ResponseParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
