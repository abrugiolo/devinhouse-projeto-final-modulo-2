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
import br.com.devinhouse.projetofinalmodulo2.utils.MascaraChaveProcesso;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
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

	private ProcessoDtoOutput converteParaDto(Processo processo) {
		return modelMapper.map(processo, ProcessoDtoOutput.class);
	}

	private Processo converteParaProcesso(ProcessoDtoInput processoDto) {
		return modelMapper.map(processoDto, Processo.class);
	}

	public ResponseEntity<?> buscarProcessosPorInteressado(Integer idInteressado) {
		if (!interessadoRepository.findById(idInteressado).isPresent()) {
			return new ResponseEntity<>("Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}
		Interessado interessado = interessadoRepository.findById(idInteressado).get();
		if (interessado.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Interessado inativo", HttpStatus.BAD_REQUEST);

		}
		List<Processo> listaProcessos = processoRepository.findBycdInteressado(interessado);
		List<ProcessoDtoOutput> listaProcessosDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessosDto, HttpStatus.OK);
	}

	public ResponseEntity<?> buscarTodosOsProcessos() {
		if (processoRepository.findAll().isEmpty()) {
			return new ResponseEntity<>("Não existem processos cadastrados.", HttpStatus.OK);
		}
		List<Processo> listaProcessos = processoRepository.findAll();
		List<ProcessoDtoOutput> listaProcessoDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessoDto, HttpStatus.OK);
	}

	public ResponseEntity<?> buscarProcessoPeloId(Integer id) {
		if (!processoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("Processo não encontrado.", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findById(id).get()));
	}

	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(Integer nuProcesso) {
		if (!processoRepository.findByNuProcesso(nuProcesso).isPresent()) {
			return new ResponseEntity<>("Processo não encontrado.", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findByNuProcesso(nuProcesso).get()));
	}

	/*
	 * 1 - Não poderá ser cadastrado um novo processo com um id já existente; ok 
	 * 2 - Não poderá ser cadastrado um novo processo com uma chave de processo já existente; ok 
	 * 3 - Não poderá ser cadastrado um novo processo com interessados inativos; 
	 * 4 - Não poderá ser cadastrado um novo processo com assuntos inativos; 
	 * 5 - Não poderá ser cadastrado um novo processo com interessados inexistentes no sistema; ok 
	 * 6 - Não poderá ser cadastrado um novo processo com assuntos inexistentes no sistema;
	 */
	public ResponseEntity<?> cadastrarProcesso(ProcessoDtoInput processoDto) {
		processoDto.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(processoDto.getSgOrgaoSetor(),
				processoDto.getNuProcesso(), processoDto.getNuAno()));

		if (processoRepository.existsByChaveProcesso(processoDto.getChaveProcesso())) {
			return new ResponseEntity<>("Chave do processo já existe no sistema.", HttpStatus.CONFLICT);
		}

		if (!interessadoRepository.findById(processoDto.getCdInteressado().getId()).isPresent()) {
			return new ResponseEntity<>("Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}
		Processo processo = converteParaProcesso(processoDto);
		processoRepository.save(processo);
		return new ResponseEntity<>("Processo criado com sucesso.", HttpStatus.OK);
	}

	/*
	 * 1 - Não poderá ser cadastrado um novo processo com um id já existente; ok 
	 * 2 - Não poderá ser cadastrado um novo processo com uma chave de processo já existente; ok 
	 * 3 - Não poderá ser cadastrado um novo processo com interessados inativos; 
	 * 4 - Não poderá ser cadastrado um novo processo com assuntos inativos; 
	 * 5 - Não poderá ser cadastrado um novo processo com interessados inexistentes no sistema; ok 
	 * 6 - Não poderá ser cadastrado um novo processo com assuntos inexistentes no sistema;
	 */
	public ResponseEntity<?> atualizarProcesso(Integer id, ProcessoDtoInput processoDto) {
		Processo processo = processoRepository.findById(id).get();
		
		if (processoDto.getSgOrgaoSetor() != null) {
			processo.setSgOrgaoSetor(processoDto.getSgOrgaoSetor());
		}
		if(processoDto.getNuProcesso() != null) {
			processo.setNuProcesso(processoDto.getNuProcesso());
		}
		if(processoDto.getNuAno() != null) {
			processo.setNuAno(processoDto.getNuAno());
		}
		if(processoDto.getDescricao() != null) {
			processo.setDescricao(processoDto.getDescricao());
		}
		if(processoDto.getCdAssunto() != null) {
			processo.setCdAssunto(processoDto.getCdAssunto());
		}
		processo.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(
				processo.getSgOrgaoSetor(), processo.getNuProcesso(), processo.getNuAno()));
		
		if (processoRepository.existsByChaveProcesso(processo.getChaveProcesso())) {
			return new ResponseEntity<>("Chave do processo já existe no sistema.", HttpStatus.CONFLICT);
		}
		
		if (!interessadoRepository.findById(processo.getCdInteressado().getId()).isPresent()) {
			return new ResponseEntity<>("Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}
		
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
		return new ResponseEntity<>("Processo atualizado com sucesso.", HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletarProcesso(Integer idProcesso) {
		processoRepository.deleteById(idProcesso);
		return new ResponseEntity<>("Processo deletado com sucesso.", HttpStatus.OK);
	}
}