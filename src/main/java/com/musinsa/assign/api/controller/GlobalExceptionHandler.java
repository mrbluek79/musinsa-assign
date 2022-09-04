package com.musinsa.assign.api.controller;

import com.musinsa.assign.exception.DataNotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleConstraintViolation(ConstraintViolationException e) {
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleException(DataAccessException e) {
        log.error(e.getMessage());

        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        final BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> errors = bindingResult.getFieldErrors();

        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(errors.parallelStream()
                        .map(error -> ExceptionResponse.FieldError.builder()
                                .reason(error.getDefaultMessage())
                                .field(error.getField())
                                .value((String) error.getRejectedValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleSessionNotFoundException(DataNotFoundException e) {
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleException(Exception e) {
        e.printStackTrace();

        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.toString())
                .build();
    }
}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ExceptionResponse {
    private int status;
    private String message;
    private List<FieldError> errors;

    @Builder
    public ExceptionResponse(int status, String message, List<FieldError> errors) {
        this.message = message;
        this.status = status;
        this.errors = (errors == null) ? new ArrayList<>() : errors;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}

