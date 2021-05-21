package br.com.devinhouse.projetofinalmodulo2.services;

import java.util.List;
import java.util.stream.Collectors;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;
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
			return new ResponseEntity<>("Nenhum processo encontrado para o interessado informado.", HttpStatus.BAD_REQUEST);
		}
		Interessado interessado = interessadoRepository.findById(idInteressado).get();
		if (interessado.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Interessado inativo.", HttpStatus.BAD_REQUEST);

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
			return new ResponseEntity<>(String.format("Nenhum processo encontrado com id '%d'.", id), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findById(id).get()));
	}

	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(Integer nuProcesso) {
		List<Processo> listaProcessos = processoRepository.findByNuProcesso(nuProcesso).orElse(null);

		// TODO: corrigido aqui: if (listaProcessos == null) {
		if (listaProcessos == null || listaProcessos.size() == 0) {
			return new ResponseEntity<>(String.format("Nenhum processo encontrado com número '%d'.", nuProcesso), HttpStatus.BAD_REQUEST);
		}

		List<ProcessoDtoOutput> listaProcessoDto = listaProcessos.stream().map(this::converteParaDto).collect(Collectors.toList());

		return new ResponseEntity<>(listaProcessoDto, HttpStatus.OK);
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
		//if (processoDto.getSgOrgaoSetor() == null || processoDto.getNuProcesso() == null || processoDto.getNuAno() == null || processoDto.getDescricao() == null || processoDto.getCdAssunto() == null || processoDto.getCdInteressado() == null) {
		if (!ValidacaoCampos.validarCamposPreenchidos(processoDto)) {
			return new ResponseEntity<>("Todos os campos devem estar preenchidos.", HttpStatus.BAD_REQUEST);
		}

		processoDto.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(processoDto.getSgOrgaoSetor(),
				processoDto.getNuProcesso(), processoDto.getNuAno()));

		if (processoRepository.existsByChaveProcesso(processoDto.getChaveProcesso())) { // TODO: confirmar msg aqui
			return new ResponseEntity<>(String.format("Já existe um processo cadastrado com a chave '%s'.", processoDto.getChaveProcesso()), HttpStatus.CONFLICT);
		}

		// TODO: codigo duplicado

		Interessado interessado = processoDto.getCdInteressado();
		if (interessado != null) {
			interessado = interessadoRepository.findById(interessado.getId()).orElse(null);

			if (interessado == null || interessado.getFlAtivo().equals('n')) {
				return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.", HttpStatus.BAD_REQUEST);
			}
		}

		Assunto assunto = processoDto.getCdAssunto();
		if (assunto != null) {
			assunto = assuntoRepository.findById(assunto.getId()).orElse(null);

			if (assunto == null || assunto.getFlAtivo().equals('n')) {
				return new ResponseEntity<>("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.", HttpStatus.BAD_REQUEST);
			}
		}

		if (!interessadoRepository.findById(processoDto.getCdInteressado().getId()).isPresent()) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}
		Processo processo = converteParaProcesso(processoDto);
		processoRepository.save(processo);
		return new ResponseEntity<>("Processo criado com sucesso.", HttpStatus.OK);
	}

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
		if(processoDto.getCdInteressado() != null) {
			processo.setCdInteressado(processoDto.getCdInteressado());
		}
		processo.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(
				processo.getSgOrgaoSetor(), processo.getNuProcesso(), processo.getNuAno()));
		
		if (processoRepository.existsByChaveProcesso(processo.getChaveProcesso())) {
			return new ResponseEntity<>("Chave do processo já existe no sistema.", HttpStatus.CONFLICT);
		}
		
		if (!interessadoRepository.findById(processo.getCdInteressado().getId()).isPresent()) {
			return new ResponseEntity<>("Interessado não encontrado.", HttpStatus.BAD_REQUEST);
		}

		Interessado interessado = processo.getCdInteressado();
		if (interessado != null) {
			interessado = interessadoRepository.findById(interessado.getId()).orElse(null);

			if (interessado == null || interessado.getFlAtivo().equals('n')) {
				return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.", HttpStatus.BAD_REQUEST);
			}
		}

		Assunto assunto = processo.getCdAssunto();
		if (assunto != null) {
			assunto = assuntoRepository.findById(assunto.getId()).orElse(null);

			if (assunto == null || assunto.getFlAtivo().equals('n')) {
				return new ResponseEntity<>("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.", HttpStatus.BAD_REQUEST);
			}
		}

		processoRepository.save(processo);
		return new ResponseEntity<>("Processo atualizado com sucesso.", HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletarProcesso(Integer idProcesso) {
		if (!processoRepository.existsById(idProcesso)) {
			return new ResponseEntity<>("Não foi possível excluir o processo: ID inválido.", HttpStatus.NOT_FOUND);
		}

		processoRepository.deleteById(idProcesso);
		return new ResponseEntity<>("Processo deletado com sucesso.", HttpStatus.OK);
	}
}
