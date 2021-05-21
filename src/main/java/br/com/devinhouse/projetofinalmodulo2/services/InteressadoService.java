package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ConversorLocalDateParaString;
import br.com.devinhouse.projetofinalmodulo2.utils.ConversorStringParaLocalDate;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class InteressadoService {

    @Autowired
    private InteressadoRepository interessadoRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Interessado converteParaInteressado(InteressadoDtoInput interessadoDtoInput) {
        modelMapper.addConverter(new ConversorStringParaLocalDate());
        return modelMapper.map(interessadoDtoInput, Interessado.class);
    }

    private InteressadoDtoOutput converteParaDto(Interessado interessado) {
        modelMapper.addConverter(new ConversorLocalDateParaString());
        return modelMapper.map(interessado, InteressadoDtoOutput.class);
    }
    
	private boolean validarNuIdentificacao(String nuIdentificacao) {
		String regex = "\\d{11}";
		return nuIdentificacao.matches(regex);
	}

    public ResponseEntity<?> buscarTodosOsInteressados() {
        List<Interessado> listaInteressados = interessadoRepository.findAll();

        if (listaInteressados.isEmpty()) {
            return new ResponseEntity<>("Não existem interessados cadastrados.", OK);
        }

        List<InteressadoDtoOutput> listaInteressadoDtoOutput = listaInteressados
        		.stream()
        		.map(this::converteParaDto)
        		.collect(Collectors.toList());

        return new ResponseEntity<>(listaInteressadoDtoOutput, OK);
    }

    public ResponseEntity<?> buscarInteressadoPeloId(Integer id) {
        Interessado interessado = interessadoRepository.findById(id).orElse(null);

        if (interessado == null) {
            return new ResponseEntity<>(String.format("Nenhum interessado encontrado com id '%d'.", id), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(interessado), OK);
    }

    public ResponseEntity<?> buscarInteressadoPeloNumeroDeIdentificacao(String nuIdentificacao) {
        Interessado interessado = interessadoRepository.findByNuIdentificacao(nuIdentificacao).orElse(null);

        if (interessado == null) {
            return new ResponseEntity<>(String.format("Nenhum interessado encontrado com número de identificação '%s'.", 
            		nuIdentificacao), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(interessado), OK);
    }

    public ResponseEntity<?> cadastrarInteressado(InteressadoDtoInput interessadoDtoInput) {

        if (!ValidacaoCampos.validarCamposPreenchidos(interessadoDtoInput)) {
            return new ResponseEntity<>("Todos os campos devem ser preenchidos.", HttpStatus.BAD_REQUEST);
        }

        if (!ValidacaoCampos.validarData(interessadoDtoInput.getDtNascimento())) {
            return new ResponseEntity<>(String.format("Data informada '%s' inválida: Deve estar no formato 'AAAA-MM-DD'.", 
            		interessadoDtoInput.getDtNascimento()), BAD_REQUEST);
        }

        if (!ValidacaoCampos.validarFlAtivo(interessadoDtoInput.getFlAtivo())) {
            return new ResponseEntity<>("Campo 'flAtivo' deve ser igual a 's' ou 'n'.", BAD_REQUEST);
        }
        
        if (!validarNuIdentificacao(interessadoDtoInput.getNuIdentificacao())) {
        	return new ResponseEntity<>("Número de identificação deve ter 11 dígitos.", BAD_REQUEST);
        }
        
        if (interessadoRepository.existsByNuIdentificacao(interessadoDtoInput.getNuIdentificacao())) {
            return new ResponseEntity<>(String.format("Já existe um cadastro com o número de identificação '%s'.", 
            		interessadoDtoInput.getNuIdentificacao()), CONFLICT);
        }

        // TODO: verificar validade do numero de identificacao
        
        Interessado interessado = converteParaInteressado(interessadoDtoInput);
        interessadoRepository.save(interessado);

        return new ResponseEntity<>("Interessado cadastrado com sucesso.", OK);
    }
}
