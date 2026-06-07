package com.a.s.APIVibraBike.service.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(String message) {
        super(message);
    }
}
