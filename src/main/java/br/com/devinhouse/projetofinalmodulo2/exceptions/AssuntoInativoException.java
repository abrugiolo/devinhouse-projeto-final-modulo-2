package br.com.devinhouse.projetofinalmodulo2.exceptions;

import org.springframework.http.HttpStatus;

public class AssuntoInativoException extends RuntimeException {

    public AssuntoInativoException(String message) {
        super(message);
    }
}
