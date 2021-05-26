package br.com.devinhouse.projetofinalmodulo2.services;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.MascaraChaveProcesso;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;

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
	
	private boolean validadorInteressado(Interessado interessado){
		if (interessado != null) {
			interessado = interessadoRepository.findById(interessado.getId()).orElse(null);

			if (interessado == null || interessado.getFlAtivo().equals('n')) {
				return true;
			}
		}
		return false;
	}
	
	private boolean validadorAssunto(Assunto assunto) {
		if (assunto != null) {
			assunto = assuntoRepository.findById(assunto.getId()).orElse(null);

			if (assunto == null || assunto.getFlAtivo().equals('n')) {
				return true;
			}
		}
		return false;
	}
	
	private Integer retornarUltimoNuProcesso() {
		List<Processo> listaProcesso = processoRepository.findAll();
		if (listaProcesso.size() == 0) {
			return 1;
		}
		return listaProcesso.get(listaProcesso.size()-1).getNuProcesso() + 1;
	}
	
	public ResponseEntity<?> buscarProcessosPorInteressado(Integer idInteressado) {
		if (!interessadoRepository.findById(idInteressado).isPresent()) {
			return new ResponseEntity<>("Nenhum processo encontrado para o interessado informado.", BAD_REQUEST);
		}
		
		Interessado interessado = interessadoRepository.findById(idInteressado).get();
		
		if (interessado.getFlAtivo().equals('n')) {
			return new ResponseEntity<>("Interessado inativo.", BAD_REQUEST);
		}
		
		List<Processo> listaProcessos = processoRepository.findByCdInteressado(interessado);
		List<ProcessoDtoOutput> listaProcessosDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());
		return new ResponseEntity<>(listaProcessosDto, OK);
	}

	public ResponseEntity<?> buscarTodosOsProcessos() {
		if (processoRepository.findAll().isEmpty()) {
			return new ResponseEntity<>("Não existem processos cadastrados.", OK);
		}
		List<Processo> listaProcessos = processoRepository.findAll();
		List<ProcessoDtoOutput> listaProcessoDto = listaProcessos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());

		return new ResponseEntity<>(listaProcessoDto, OK);
	}

	public ResponseEntity<?> buscarProcessoPeloId(Integer id) {
		if (!processoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>(String.format("Nenhum processo encontrado com id '%d'.", id), 
					BAD_REQUEST);
		}
		return ResponseEntity.ok(converteParaDto(processoRepository.findById(id).get()));
	}

	public ResponseEntity<?> buscarProcessoPeloNumeroProcesso(Integer nuProcesso) {
		Processo processo = processoRepository.findByNuProcesso(nuProcesso).orElse(null);

		if (processo == null) {
			return new ResponseEntity<>(String.format("Nenhum processo encontrado com número '%d'.", nuProcesso), 
					BAD_REQUEST);
		}

		ProcessoDtoOutput processoDto = converteParaDto(processo);

		return new ResponseEntity<>(processoDto, OK);
	}

	public ResponseEntity<?> cadastrarProcesso(ProcessoDtoInput processoDto) {
		if (!ValidacaoCampos.validarCamposPreenchidos(processoDto)) {
			return new ResponseEntity<>("Todos os campos devem estar preenchidos.", BAD_REQUEST);
		}
		
		processoDto.setNuProcesso(retornarUltimoNuProcesso());
		processoDto.setChaveProcesso(MascaraChaveProcesso.gerarChaveProcesso(processoDto.getSgOrgaoSetor(),
				processoDto.getNuProcesso(), processoDto.getNuAno()));

		if (processoRepository.existsByChaveProcesso(processoDto.getChaveProcesso())) {
			return new ResponseEntity<>(String.format("Já existe um processo cadastrado com a chave '%s'.", 
					processoDto.getChaveProcesso()), CONFLICT);
		}

		Interessado interessado = processoDto.getCdInteressado();
		if (validadorInteressado(interessado)) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.", 
					BAD_REQUEST);
		}

		Assunto assunto = processoDto.getCdAssunto();
		if (validadorAssunto(assunto)) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.", 
					BAD_REQUEST);
		}

		if (!interessadoRepository.findById(processoDto.getCdInteressado().getId()).isPresent()) {
			return new ResponseEntity<>("Não foi possível cadastrar o processo: Interessado não encontrado.", 
					BAD_REQUEST);
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
			return new ResponseEntity<>("Chave do processo já existe no sistema.", CONFLICT);
		}

		Interessado interessado = processo.getCdInteressado();
		if (validadorInteressado(interessado)) {
			return new ResponseEntity<>("Não foi possível atualizar o processo: Interessado inativo ou inexistente.", 
					BAD_REQUEST);
		}

		Assunto assunto = processo.getCdAssunto();
		if(validadorAssunto(assunto)) {
			return new ResponseEntity<>("Não foi possível atualizar o processo: Assunto inativo ou inexistente.", 
					BAD_REQUEST);
		}

		processoRepository.save(processo);
		return new ResponseEntity<>("Processo atualizado com sucesso.", OK);
	}
	
	public ResponseEntity<?> deletarProcesso(Integer idProcesso) {
		if (!processoRepository.existsById(idProcesso)) {
			return new ResponseEntity<>("Não foi possível excluir o processo: ID inválido.", NOT_FOUND);
		}

		processoRepository.deleteById(idProcesso);
		return new ResponseEntity<>("Processo deletado com sucesso.", OK);
	}
}
