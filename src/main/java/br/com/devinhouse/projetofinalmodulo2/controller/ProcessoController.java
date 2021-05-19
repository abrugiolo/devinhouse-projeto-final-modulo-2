package br.com.devinhouse.projetofinalmodulo2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/processo")
public class ProcessoController {
	
	@Autowired
	private ProcessoService processoService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(path = "/{interessado}")
	public ResponseEntity<List<ProcessoDto>> buscarProcessosPorInteressado(@PathVariable Interessado interessado) {
		List<Processo> listaProcessos = processoService.buscarProcessosPorInteressado(interessado);
		List<ProcessoDto> listaProcessosDto = listaProcessos
				.stream()
				.map(this::converteParaDto)
				.collect(Collectors.toList());
		return ResponseEntity.ok(listaProcessosDto);
	}
	
	private ProcessoDto converteParaDto(Processo processo) {
		return modelMapper.map(processo, ProcessoDto.class);
	}

	@GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
	public List<Processo> listarTodosOsProcessos() {

		return processoService.buscarTodosOsProcessos();
	}

	@GetMapping(value = "/id/{id}", produces = APPLICATION_JSON_VALUE)
	public Processo buscarProcessoPeloId(@PathVariable Integer id) {

		return processoService.buscarProcessoPeloId(id);
	}

	// ERRO AQUI!
	@GetMapping(value = "/numero/{nuProcesso}", produces = APPLICATION_JSON_VALUE)
	public Processo buscarProcessoPeloNumero(@PathVariable Integer nuProcesso) {

		return processoService.buscarProcessoPeloNumero(nuProcesso);
	}

	@PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public Processo cadastrarProcesso(@RequestBody Processo processo) {

		return processoService.cadastrarProcesso(processo);
	}
}