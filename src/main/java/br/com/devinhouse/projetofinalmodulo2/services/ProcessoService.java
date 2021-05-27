package br.com.devinhouse.projetofinalmodulo2.services;

import java.util.List;
import java.util.stream.Collectors;

import br.com.devinhouse.projetofinalmodulo2.exceptions.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.MascaraChaveProcesso;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;

import static org.springframework.http.HttpStatus.*;

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

	private Integer retornarUltimoNuProcesso() {
		List<Processo> listaProcesso = processoRepository.findAll();
		if (listaProcesso.size() == 0) {
			return 1;
		}
		return listaProcesso.get(listaProcesso.size() - 1).getNuProcesso() + 1;
	}

	public ResponseEntity<?> buscarProcessosPorInteressado(Integer idInteressado) {
		if (interessadoRepository.findById(idInteressado).isEmpty()) {
			throw new NotFoundException("Nenhum processo encontrado para o interessado informado.");
		}
	
		Interessado interessado = interessadoRepository.findById(idInteressado).get();
		if (interessado.getFlAtivo().equals('n')) {
			throw new InteressadoInativoException("Não foi possível efetuar a busca: Interessado inativo.");
		}
	
		List<Processo> listaProcessos = processoRepository.findByCdInteressado(interessado);
		List<ProcessoDtoOutput> listaProcessosDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessosDto, OK);
	}
	
	public ResponseEntity<?> buscarTodosOsProcessos() {
		List<Processo> listaProcessos = processoRepository.findAll();
		if (listaProcessos.isEmpty()) {
			throw new NotFoundException("Não existem processos cadastrados.");
		}
		List<ProcessoDtoOutput> listaProcessoDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());

		return new ResponseEntity<>(listaProcessoDto, OK);
	}

	public ResponseEntity<?> buscarProcessoPeloId(Integer id) {
		if (processoRepository.findById(id).isEmpty()) {
			throw new NotFoundException(String.format("Nenhum processo encontrado com id '%d'.", id));
		}
		return new ResponseEntity<>(converteParaDto(processoRepository.findById(id).get()), OK);
	}

	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(Integer nuProcesso) {
		Processo processo = processoRepository.findByNuProcesso(nuProcesso).orElse(null);

		if (processo == null) {
			throw new NotFoundException(String.format("Nenhum processo encontrado com número '%d'.", nuProcesso));
		}
		
		ProcessoDtoOutput processoDto = converteParaDto(processo);

		return new ResponseEntity<>(processoDto, OK);
	}

	public ResponseEntity<?> cadastrarProcesso(ProcessoDtoInput processoDto) {
		if (!ValidacaoCampos.validarCamposPreenchidos(processoDto)) {
			throw new CampoVazioException("Todos os campos devem estar preenchidos.");
		}
		
		processoDto.setNuProcesso(retornarUltimoNuProcesso());
		processoDto.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(processoDto.getSgOrgaoSetor(),
				processoDto.getNuProcesso(), processoDto.getNuAno()));

		if (processoRepository.existsByChaveProcesso(processoDto.getChaveProcesso())) {
			throw new ChaveProcessoJaExisteException(String.format("Já existe um processo cadastrado com a chave '%s'.",
					processoDto.getChaveProcesso()));
		}

		if (ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDto.getCdInteressado())) {
			throw new InteressadoInativoException(
					"Não foi possível cadastrar o processo: Interessado inativo ou inexistente.");
		}

		if (ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDto.getCdAssunto())) {
			throw new AssuntoInativoException("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.");
		}

		Processo processo = converteParaProcesso(processoDto);
		processoRepository.save(processo);
		return new ResponseEntity<>("Processo criado com sucesso.", CREATED);
	}

	public ResponseEntity<?> atualizarProcesso(Integer id, ProcessoDtoInput processoDto) {
		Processo processo = processoRepository.findById(id).get();

		if (processoDto.getSgOrgaoSetor() != null) {
			processo.setSgOrgaoSetor(processoDto.getSgOrgaoSetor());
		}
		if (processoDto.getNuProcesso() != null) {
			processo.setNuProcesso(processoDto.getNuProcesso());
		}
		if (processoDto.getNuAno() != null) {
			processo.setNuAno(processoDto.getNuAno());
		}
		if (processoDto.getDescricao() != null) {
			processo.setDescricao(processoDto.getDescricao());
		}
		if (processoDto.getCdAssunto() != null) {
			processo.setCdAssunto(processoDto.getCdAssunto());
		}
		if (processoDto.getCdInteressado() != null) {
			processo.setCdInteressado(processoDto.getCdInteressado());
		}
		processo.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(processo.getSgOrgaoSetor(),
				processo.getNuProcesso(), processo.getNuAno()));

		if (processoRepository.existsByChaveProcesso(processo.getChaveProcesso())) {
			throw new ChaveProcessoJaExisteException("Chave do processo já existe no sistema.");
		}

		if (ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDto.getCdInteressado())) {
			throw new InteressadoInativoException(
					"Não foi possível cadastrar o processo: Interessado inativo ou inexistente.");
		}

		if (ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDto.getCdAssunto())) {
			throw new AssuntoInativoException("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.");
		}

		processoRepository.save(processo);
		return new ResponseEntity<>("Processo atualizado com sucesso.", OK);
	}

	public ResponseEntity<?> deletarProcesso(Integer idProcesso) {
		if (!processoRepository.existsById(idProcesso)) {
			throw new NotFoundException("Não foi possível excluir o processo: ID inválido.");
		}

		processoRepository.deleteById(idProcesso);
		return new ResponseEntity<>("Processo deletado com sucesso.", OK);
	}
}
