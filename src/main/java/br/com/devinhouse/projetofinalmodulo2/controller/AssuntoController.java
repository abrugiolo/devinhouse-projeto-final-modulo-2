package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.AssuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="/assunto")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @GetMapping(path="/lista", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarTodosOsAssuntos() {

        return assuntoService.buscarTodosOsAssuntos();
    }

    @GetMapping(path="/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarAssuntoPeloId(@PathVariable Integer id) {

        return assuntoService.buscarAssuntoPeloId(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarAssunto(@RequestBody AssuntoDtoInput assuntoDtoInput) {

        return assuntoService.cadastrarAssunto(assuntoDtoInput);
    }

}
