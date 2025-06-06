package com.renatmirzoev.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OutOfMoneyException extends RuntimeException {

    public OutOfMoneyException(String message) {
        super(message);
    }
}
