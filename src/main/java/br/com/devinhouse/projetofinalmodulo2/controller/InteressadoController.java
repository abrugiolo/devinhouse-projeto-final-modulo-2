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

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pesquisarInteressado(@RequestParam(name = "id", required = false) Integer id, @RequestParam(name = "identificacao", required = false) String identificacao) {
        if (id != null) {
            return interessadoService.buscarInteressadoPeloId(id);
        }
        if (identificacao != null) {
            return interessadoService.buscarInteressadoPeloNumeroDeIdentificacao(identificacao);
        }
        return interessadoService.buscarTodosOsInteressados();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrarInteressado(@RequestBody InteressadoDtoInput interessadoDtoInput) {

        return interessadoService.cadastrarInteressado(interessadoDtoInput);
    }

}
