package br.com.devinhouse.projetofinalmodulo2.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
@Service
public class ProcessoService {
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	
	@Autowired 
	private InteressadoRepository interessadoRepository;
	 
	@Autowired
	private AssuntoRepository assuntoRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	private ProcessoDto converteParaDto(Processo processo) {
		return modelMapper.map(processo, ProcessoDto.class);
	}
	
	private Processo converteParaProcesso(ProcessoDto processoDto) {
		return modelMapper.map(processoDto, Processo.class);
	}
	
	public ResponseEntity<?> buscarProcessosPorInteressado (Integer idInteressado) {
		if (!interessadoRepository.findById(idInteressado).isPresent()) {
			return new ResponseEntity<>("Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}
		
		Interessado interessado = interessadoRepository.findById(idInteressado).get();
		
		//tratamento para interessados inativos ?
		//Manter essa parte?
//		if (interessado.getFlAtivo() == 'n') {
		if (interessado.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Interessado inativo", HttpStatus.BAD_REQUEST);
		}
		
		List<Processo> listaProcessos = processoRepository.findBycdInteressado(interessado);
		List<ProcessoDto> listaProcessosDto = listaProcessos
				.stream()
				.map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessosDto, HttpStatus.OK);
	}

	public ResponseEntity<?> buscarTodosOsProcessos() {
		if (processoRepository.findAll().isEmpty()) {
			return new ResponseEntity<>("Não existem processos cadastrados", HttpStatus.OK);
		}
		List<Processo> listaProcessos = processoRepository.findAll();
		List<ProcessoDto> listaProcessoDto = listaProcessos
				.stream()
				.map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessoDto, HttpStatus.OK);
	}

	public ResponseEntity<?> buscarProcessoPeloId(Integer id) {
		if (!processoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("Processo não encontrado", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findById(id).get()));
	}

	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(Integer nuProcesso) {
		if (!processoRepository.findByNuProcesso(nuProcesso).isPresent()) {
			return new ResponseEntity<>("Processo não encontrado", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findByNuProcesso(nuProcesso).get()));
	}

	public ResponseEntity<?> cadastrarProcesso(ProcessoDto processoDto) {
		Processo processo = converteParaProcesso(processoDto);
		
		if (processoRepository.existsByChaveProcesso(processo.getChaveProcesso()))
			return new ResponseEntity<>("Não foi possível efetuar o cadastro: Chave do Processo já existe no sistema.", HttpStatus.BAD_REQUEST);

		// TODO: verificar se interessado e assunto existem e estao ativos

		Interessado interessado = interessadoRepository.findById(processo.getCdInteressado().getId()).orElse(null);
		if (interessado != null && interessado.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado inativo.", HttpStatus.BAD_REQUEST);
		}

		Assunto assunto = assuntoRepository.findById(processo.getCdAssunto().getId()).orElse(null);
		if (assunto != null && assunto.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Assunto inativo.", HttpStatus.BAD_REQUEST);
		}

		processoRepository.save(processo);
		return new ResponseEntity<>("Processo criado com sucesso", HttpStatus.OK);
	}

}
