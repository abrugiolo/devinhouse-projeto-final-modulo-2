package br.com.devinhouse.projetofinalmodulo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/processo")
public class ProcessoController {

	@Autowired
	private ProcessoService processoService;
	
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pesquisarProcesso(@RequestParam(name = "id-interessado", required = false) Integer idInteressado,
												@RequestParam(name = "id-processo", required = false) Integer idProcesso,
												@RequestParam(name = "nu-processo", required = false) Integer nuProcesso) {
		if (idInteressado != null) {
			return processoService.buscarProcessosPorInteressado(idInteressado);
		}
		if (idProcesso != null) {
			return processoService.buscarProcessoPeloId(idProcesso);
		}
		if (nuProcesso != null) {
			return processoService.buscarProcessoPeloNumeroProcesso(nuProcesso);
		}
		return processoService.buscarTodosOsProcessos();
	}

	/*
	 * //passar valor por request param exemplo: BASE_URL/processo?idInteressado=2
	 * 
	 * @GetMapping(path = "/interessado", produces = APPLICATION_JSON_VALUE) public
	 * ResponseEntity<?> buscarProcessosPorInteressado(@RequestParam Integer
	 * idInteressado) { return
	 * processoService.buscarProcessosPorInteressado(idInteressado); }
	 * 
	 * @GetMapping(path = "/lista", produces = APPLICATION_JSON_VALUE) public
	 * ResponseEntity<?> listarTodosOsProcessos() { return
	 * processoService.buscarTodosOsProcessos(); }
	 * 
	 * @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE) public
	 * ResponseEntity<?> buscarProcessoPeloId(@PathVariable Integer id) { return
	 * processoService.buscarProcessoPeloId(id); }
	 * 
	 * // ERRO AQUI! //corrigir o erro. Ambiguidade nos paths
	 * 
	 * @GetMapping(produces = APPLICATION_JSON_VALUE) public ResponseEntity<?>
	 * buscarProcessoPeloNumeroProcesso(@RequestParam Integer nuProcesso) { return
	 * processoService.buscarProcessoPeloNumeroProcesso(nuProcesso); }
	 */

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cadastrarProcesso(@RequestBody ProcessoDtoInput processo) {
		return processoService.cadastrarProcesso(processo);
	}
	
	@PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> atualizarCadastro(@PathVariable Integer idProcesso, @RequestBody ProcessoDtoInput processoDto) {
		return processoService.atualizarProcesso(idProcesso, processoDto);
	}
	
	@DeleteMapping(path = "/{id}") 
	public ResponseEntity<?> deletarProcesso(@PathVariable Integer idProcesso) {
		return processoService.deletarProcesso(idProcesso);
	}
}