package com.renatmirzoev.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerAlreadyExistException extends RuntimeException {

    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}
