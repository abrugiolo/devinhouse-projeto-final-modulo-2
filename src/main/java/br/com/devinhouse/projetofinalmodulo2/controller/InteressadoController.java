package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.services.InteressadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="/interessado")
public class InteressadoController {

    @Autowired
    private InteressadoService interessadoService;

    @GetMapping(path="/lista", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarTodosOsInteressados() {

        return interessadoService.buscarTodosOsInteressados();
    }

    @GetMapping(path="/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarInteressadoPeloId(@PathVariable Integer id) {

        return interessadoService.buscarInteressadoPeloId(id);
    }

    @GetMapping(path="/identificacao/{nuIdentificacao}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarInteressadoPeloNumeroDeIdentificacao(@PathVariable String nuIdentificacao) {

        return interessadoService.buscarInteressadoPeloNumeroDeIdentificacao(nuIdentificacao);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarInteressado(@RequestBody InteressadoDtoInput interessadoDtoInput) {

        return interessadoService.cadastrarInteressado(interessadoDtoInput);
    }

}
