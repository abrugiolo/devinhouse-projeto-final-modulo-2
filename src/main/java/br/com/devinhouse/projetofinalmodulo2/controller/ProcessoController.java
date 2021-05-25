package br.com.devinhouse.projetofinalmodulo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/processo/v1")
public class ProcessoController {

	@Autowired
	private ProcessoService processoService;
	
	@GetMapping(path = {"", "/{id}"}, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pesquisarProcesso(@PathVariable(required = false) Integer id,
											   @RequestParam(name = "nu_processo", required = false) Integer nuProcesso,
											   @RequestParam(name = "id_interessado", required = false) Integer idInteressado) {
		if (id != null) {
			return processoService.buscarProcessoPeloId(id);
		}
		if (nuProcesso != null) {
			return processoService.buscarProcessoPeloNumeroProcesso(nuProcesso);
		}
		if (idInteressado != null) {
			return processoService.buscarProcessosPorInteressado(idInteressado);
		}
		return processoService.buscarTodosOsProcessos();
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarProcesso(@RequestBody ProcessoDtoInput processo) {
		return processoService.cadastrarProcesso(processo);
	}
	
	@PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> atualizarCadastro(@PathVariable Integer id, @RequestBody ProcessoDtoInput processoDto) {
		return processoService.atualizarProcesso(id, processoDto);
	}
	
	@DeleteMapping(path = "/{id}") 
	public ResponseEntity<?> deletarProcesso(@PathVariable Integer id) {
		return processoService.deletarProcesso(id);
	}
}