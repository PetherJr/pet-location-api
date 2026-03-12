package com.petherson.petlocation.presentation.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create("https://api.petlocation.com/errors/validation"));
        return problemDetail;
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ProblemDetail handleMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request");
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String title = "Internal Server Error";
        
        boolean isGeocodingError = ex.getMessage().toLowerCase().contains("geocoding provider");
        boolean isTimeout = isTimeout(ex);

        if (isGeocodingError) {
            if (isTimeout) {
                status = HttpStatus.GATEWAY_TIMEOUT;
                title = "Provider Timeout";
            } else {
                status = HttpStatus.BAD_GATEWAY;
                title = "Provider Failure";
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(title);
        return problemDetail;
    }

    private boolean isTimeout(Throwable ex) {
        Throwable cause = ex;
        while (cause != null) {
            if (cause instanceof java.net.SocketTimeoutException || 
                cause.getClass().getName().contains("TimeoutException")) {
                return true;
            }
            String msg = cause.getMessage();
            if (msg != null && (msg.toLowerCase().contains("timeout") || msg.toLowerCase().contains("timed out"))) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}
