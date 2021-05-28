package br.com.devinhouse.projetofinalmodulo2.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), NOT_FOUND, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, NOT_FOUND);

    }

    @ExceptionHandler(value = { DataInvalidaException.class })
    protected ResponseEntity<Object> handleDataInvalidaException(DataInvalidaException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { CampoVazioException.class })
    protected ResponseEntity<Object> handleCampoInvalidoException(CampoVazioException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { FlAtivoInvalidoException.class })
    protected ResponseEntity<Object> handleFlAtivoInvalidoException(FlAtivoInvalidoException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { NuIdentificacaoInvalidoException.class })
    protected ResponseEntity<Object> handleNuIdentificacaoInvalidoException(NuIdentificacaoInvalidoException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { NuIdentificacaoJaExisteException.class })
    protected ResponseEntity<Object> handleNuIdentificacaoJaExisteException(NuIdentificacaoJaExisteException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), CONFLICT, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, CONFLICT);
    }

    @ExceptionHandler(value = { InteressadoInativoException.class })
    protected ResponseEntity<Object> handleInteressadoInativoException(InteressadoInativoException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { AssuntoInativoException.class })
    protected ResponseEntity<Object> handleAssuntoInativoException(AssuntoInativoException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), BAD_REQUEST, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(value = { ChaveProcessoJaExisteException.class })
    protected ResponseEntity<Object> handleChaveProcessoJaExisteException(ChaveProcessoJaExisteException exception, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), CONFLICT, exception.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI());

        return new ResponseEntity<>(response, CONFLICT);
    }

}
