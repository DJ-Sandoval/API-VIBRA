package com.a.s.APIVibraBike.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando el horario solicitado no opera el día de la semana
 * de la fecha elegida (ej.: SEXTA_CLASE un sábado).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HorarioNoDisponibleException extends RuntimeException {

    public HorarioNoDisponibleException(String message) {
        super(message);
    }
}

