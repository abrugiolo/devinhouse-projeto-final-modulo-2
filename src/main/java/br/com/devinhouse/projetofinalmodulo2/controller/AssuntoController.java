package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.AssuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="/assunto/v1")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @GetMapping(path = {"", "/{id}"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pesquisarAssunto(@PathVariable(required = false) Integer id) {
        if (id != null) return assuntoService.buscarAssuntoPeloId(id);

        return assuntoService.buscarTodosOsAssuntos();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarAssunto(@RequestBody AssuntoDtoInput assuntoDtoInput) {

        return assuntoService.cadastrarAssunto(assuntoDtoInput);
    }
}
