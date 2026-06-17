package com.a.s.APIVibraBike.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta reservar una bici que ya está ocupada
 * en el mismo slot de fecha + horario.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class BiciOcupadaException extends RuntimeException {

    public BiciOcupadaException(String message) {
        super(message);
    }
}
