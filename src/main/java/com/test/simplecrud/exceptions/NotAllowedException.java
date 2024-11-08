package com.test.simplecrud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotAllowedException extends RuntimeException {

    public NotAllowedException(Errors error) {
        super(error.name());
    }

    public NotAllowedException() {
        super();
    }

}
