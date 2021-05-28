package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.InteressadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="/interessado/v1")
public class InteressadoController {

    @Autowired
    private InteressadoService interessadoService;

    @GetMapping(path = {"", "/{id}"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pesquisarInteressado(@PathVariable(required = false) Integer id,
                                                  @RequestParam(name = "nu_identificacao", required = false) String nuIdentificacao) {
        if (id != null) {
            return interessadoService.buscarInteressadoPeloId(id);
        }
        if (nuIdentificacao != null) {
            return interessadoService.buscarInteressadoPeloNumeroDeIdentificacao(nuIdentificacao);
        }
        return interessadoService.buscarTodosOsInteressados();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarInteressado(@RequestBody InteressadoDtoInput interessadoDtoInput) {

        return interessadoService.cadastrarInteressado(interessadoDtoInput);
    }

}
