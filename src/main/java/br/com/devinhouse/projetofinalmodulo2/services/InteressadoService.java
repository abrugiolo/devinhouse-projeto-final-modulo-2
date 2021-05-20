package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        return modelMapper.map(interessadoDtoInput, Interessado.class);
    }

    private InteressadoDtoOutput converteParaDto(Interessado interessado) {
        return modelMapper.map(interessado, InteressadoDtoOutput.class);
    }

    public ResponseEntity<?> buscarTodosOsInteressados() {
        List<Interessado> listaInteressados = interessadoRepository.findAll();

        if (listaInteressados.isEmpty()) {
            return new ResponseEntity<>("Não existem interessados cadastrados", OK);
        }

        List<InteressadoDtoOutput> listaInteressadoDtoOutput = listaInteressados.stream().map(this::converteParaDto).collect(Collectors.toList());

        return new ResponseEntity<>(listaInteressadoDtoOutput, OK);
    }

    public ResponseEntity<?> buscarInteressadoPeloId(Integer id) {
        Interessado interessado = interessadoRepository.findById(id).orElse(null);

        if (interessado == null) {
            return new ResponseEntity<>(String.format("Interessado não encontrado (id = %d)", id), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(interessado), OK);
    }

    public ResponseEntity<?> buscarInteressadoPeloNumeroDeIdentificacao(String nuIdentificacao) {
        Interessado interessado = interessadoRepository.findByNuIdentificacao(nuIdentificacao).orElse(null);

        if (interessado == null) {
            return new ResponseEntity<>(String.format("Interessado não encontrado (nuIdentificacao = '%s')", nuIdentificacao), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(interessado), OK);
    }

    public ResponseEntity<?> cadastrarInteressado(InteressadoDtoInput interessadoDtoInput) {

        if (interessadoDtoInput.getNmInteressado() == null || interessadoDtoInput.getNuIdentificacao() == null || interessadoDtoInput.getDtNascimento() == null || interessadoDtoInput.getFlAtivo() == null) {
            return new ResponseEntity<>("Todos os campos devem ser preenchidos.", HttpStatus.BAD_REQUEST);
        }

        if (interessadoRepository.existsByNuIdentificacao(interessadoDtoInput.getNuIdentificacao())) {
            return new ResponseEntity<>(String.format("Número de Identificação já existe no sistema (nuIdentificacao = '%s')", interessadoDtoInput.getNuIdentificacao()), CONFLICT);
        }

        // TODO: verificar validade do numero de identificacao

        Interessado interessado = converteParaInteressado(interessadoDtoInput);
        interessadoRepository.save(interessado);

        return new ResponseEntity<>("Interessado cadastrado com sucesso", OK);
    }

}
