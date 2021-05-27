package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.exceptions.CampoVazioException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.DataInvalidaException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.FlAtivoInvalidoException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.NotFoundException;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ConversorLocalDateParaString;
import br.com.devinhouse.projetofinalmodulo2.utils.ConversorStringParaLocalDate;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class AssuntoService {

	@Autowired
	private AssuntoRepository assuntoRepository;

	@Autowired
	private ModelMapper modelMapper;

	private Assunto converteParaAssunto(AssuntoDtoInput assuntoDtoInput) {
		modelMapper.addConverter(new ConversorStringParaLocalDate());
		return modelMapper.map(assuntoDtoInput, Assunto.class);
	}

	private AssuntoDtoOutput converteParaDto(Assunto assunto) {
		modelMapper.addConverter(new ConversorLocalDateParaString());
		return modelMapper.map(assunto, AssuntoDtoOutput.class);
	}

	public ResponseEntity<?> buscarTodosOsAssuntos() {
		List<Assunto> listaAssuntos = assuntoRepository.findAll();

		if (listaAssuntos.isEmpty()) {
			return new ResponseEntity<>("Não existem assuntos cadastrados.", OK);
		}

		List<AssuntoDtoOutput> listaAssuntoDtoOutput = listaAssuntos.stream().map(this::converteParaDto)
				.collect(Collectors.toList());

		return new ResponseEntity<>(listaAssuntoDtoOutput, OK);
	}

	public ResponseEntity<?> buscarAssuntoPeloId(Integer id) {
		Assunto assunto = assuntoRepository.findById(id).orElse(null);

		if (assunto == null) {
			throw new NotFoundException(String.format("Nenhum assunto encontrado com id '%d'.", id));
		}

		return new ResponseEntity<>(converteParaDto(assunto), OK);
	}

	public ResponseEntity<?> cadastrarAssunto(AssuntoDtoInput assuntoDtoInput) {

		if (!ValidacaoCampos.validarCamposPreenchidos(assuntoDtoInput)) {
			throw new CampoVazioException("Todos os campos devem ser preenchidos.");
		}

		if (!ValidacaoCampos.validarData(assuntoDtoInput.getDtCadastro())) {
			throw new DataInvalidaException(
					String.format("Data informada '%s' inválida: Deve estar no formato 'AAAA-MM-DD'.",
							assuntoDtoInput.getDtCadastro()));
		}

		if (!ValidacaoCampos.validarFlAtivo(assuntoDtoInput.getFlAtivo())) {
			throw new FlAtivoInvalidoException("Campo 'flAtivo' deve ser igual a 's' ou 'n'.");
		}

		Assunto assunto = converteParaAssunto(assuntoDtoInput);
		assuntoRepository.save(assunto);

		return new ResponseEntity<>("Assunto cadastrado com sucesso.", CREATED);
	}
}
