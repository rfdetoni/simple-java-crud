package com.test.simplecrud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CypherException extends RuntimeException {


    public CypherException(Errors error) {
        super(error.name());
    }

    public CypherException() {
        super();
    }

}
