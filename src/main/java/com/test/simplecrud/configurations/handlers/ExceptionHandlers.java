package com.test.simplecrud.configurations.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.test.simplecrud.exceptions.NotAllowedException;
import com.test.simplecrud.exceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoSuchElementException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,  ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleNewAccountException(JsonMappingException ex, WebRequest request) {
        String bodyOfResponse = "Invalid transaction type";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {AccessDeniedException.class, NotAllowedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<Object> handleAccessDeniedException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}