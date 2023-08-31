package com.mkan.controller;

import com.mkan.controller.dto.ExceptionMessage;
import com.mkan.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS = Map.of(
            ConstraintViolationException.class, HttpStatus.BAD_REQUEST,
            DataIntegrityViolationException.class, HttpStatus.BAD_REQUEST,
            UserNotFoundException.class, HttpStatus.NOT_FOUND,
            HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE,
            RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR
    );

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception exception,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        log.error("Exception: {}, HttpStatus={}", exception, statusCode);
        return super.handleExceptionInternal(
                exception,
                new ExceptionMessage(statusCode.value(), exception.getMessage()),
                headers, statusCode, request);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            @NonNull HttpMediaTypeNotAcceptableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        headers.put(HttpHeaders.CONTENT_TYPE, List.of("application/json"));
        log.error("Media type {} not acceptable, HttpStatus={}", headers.getAccept(), statusCode);
        return handleExceptionInternal(ex,
                new ExceptionMessage(statusCode.value(), "Requested media type is not supported"),
                headers, HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(final Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
    }

    private ResponseEntity<Object> doHandle(final Exception exception, final HttpStatus status) {
        log.error("Exception: {}, HttpStatus={}", exception, status);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionMessage(status.value(), exception.getMessage()));
    }

    public HttpStatus getHttpStatusFromException(final Class<?> exception) {
        return EXCEPTION_STATUS.getOrDefault(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}