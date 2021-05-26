package br.com.devinhouse.projetofinalmodulo2.exceptions;

import org.springframework.http.HttpStatus;

public class InteressadoInativoException extends RuntimeException {

    public InteressadoInativoException(String message) {
        super(message);
    }
}
