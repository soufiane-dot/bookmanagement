package com.api.bookmanagement.resource.error;

import com.api.bookmanagement.config.Messages;
import com.api.bookmanagement.dto.error.ResponseError;
import com.api.bookmanagement.exception.FunctionalException;
import com.api.bookmanagement.exception.TechnicalException;
import com.api.bookmanagement.util.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Messages messages;

    @ExceptionHandler({TechnicalException.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleTechnicalException(Exception e) {
        return buildResponseError(e.getClass().getSimpleName(),
                messages.get(GlobalConstants.ERROR_WS_TECHNICAL),
                GlobalConstants.URI_TECHNICAL_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FunctionalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleFunctionalException(FunctionalException e) {
        return buildResponseError(e.getClass().getSimpleName(),
                e.getMessage(),
                GlobalConstants.URI_FUNCTIONAL_EXCEPTION,
                HttpStatus.BAD_REQUEST);
    }

    private ResponseError buildResponseError(String title, String detail, String type, HttpStatus status) {
        return ResponseError.builder()
                .title(title)
                .detail(detail)
                .type(type)
                .status(String.valueOf(status.value()))
                .build();
    }
}
