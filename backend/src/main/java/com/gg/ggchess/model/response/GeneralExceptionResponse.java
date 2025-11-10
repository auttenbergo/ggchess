package com.gg.ggchess.model.response;

import lombok.Getter;

@Getter
public class GeneralExceptionResponse {

    private final String message;
    private final Integer httpStatusCode;

    private GeneralExceptionResponse(String message, Integer httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static GeneralExceptionResponse of(String message, Integer httpStatusCode) {
        return new GeneralExceptionResponse(message, httpStatusCode);
    }

}
