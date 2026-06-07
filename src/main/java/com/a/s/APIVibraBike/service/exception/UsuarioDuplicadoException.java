package com.a.s.APIVibraBike.service.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String message) {
        super(message);
    }
}
