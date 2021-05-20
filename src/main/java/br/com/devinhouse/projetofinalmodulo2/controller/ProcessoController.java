package br.com.devinhouse.projetofinalmodulo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDto;
import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/processo")
public class ProcessoController {
	
	@Autowired
	private ProcessoService processoService;

	//passar valor por request param exemplo: BASE_URL/processo?idInteressado=2
	@GetMapping(path = "/interessado", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarProcessosPorInteressado(@RequestParam Integer idInteressado) {
		return processoService.buscarProcessosPorInteressado(idInteressado);
	}

	@GetMapping(path = "/lista", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listarTodosOsProcessos() {
		return processoService.buscarTodosOsProcessos();
	}

	@GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarProcessoPeloId(@PathVariable Integer id) {
		return processoService.buscarProcessoPeloId(id);
	}

	// ERRO AQUI!
	//corrigir o erro. Ambiguidade nos paths
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(@RequestParam Integer nuProcesso) {
		return processoService.buscarProcessoPeloNumeroProcesso(nuProcesso);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?>cadastrarProcesso(@RequestBody ProcessoDto processo) {
		return processoService.cadastrarProcesso(processo);
	}
}