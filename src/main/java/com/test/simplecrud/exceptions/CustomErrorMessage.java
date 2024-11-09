package com.test.simplecrud.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
public class CustomErrorMessage {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public CustomErrorMessage(HttpStatus status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }
}