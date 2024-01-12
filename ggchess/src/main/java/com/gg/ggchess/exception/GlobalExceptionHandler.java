package com.gg.ggchess.exception;

import com.gg.ggchess.model.response.GeneralExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GeneralExceptionResponse> handleResponseStatusException(ResponseStatusException exception) {
        String message = exception.getReason();
        int httpStatusCode = exception.getStatusCode().value();
        HttpStatus httpStatus = HttpStatus.valueOf(httpStatusCode);
        logError(exception, ResponseStatusException.class);
        return new ResponseEntity<>(GeneralExceptionResponse.of(message, httpStatusCode), httpStatus);
    }

    private void logError(Exception ex, Class<?> exceptionClass) {
        log.error("Exception of class: {} thrown, with message: {}", exceptionClass.getSimpleName(), ex.getMessage());
        log.error("Stacktrace: ", ex);
    }

}
