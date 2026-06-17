package com.a.s.APIVibraBike.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando un usuario intenta reservar un slot (fecha + horario)
 * en el que ya tiene una reserva activa.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ReservaDuplicadaException extends RuntimeException {

    public ReservaDuplicadaException(String message) {
        super(message);
    }
}
