package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Assunto converteParaAssunto(AssuntoDtoInput assuntoDtoInput) {
        return modelMapper.map(assuntoDtoInput, Assunto.class);
    }

    private AssuntoDtoInput converteParaDto(Assunto assunto) {
        return modelMapper.map(assunto, AssuntoDtoInput.class);
    }

    public ResponseEntity<?> buscarTodosOsAssuntos() {
        List<Assunto> listaAssuntos = assuntoRepository.findAll();

        if (listaAssuntos.isEmpty()) {
            return new ResponseEntity<>("Não existem assuntos cadastrados", OK);
        }

        List<AssuntoDtoInput> listaAssuntoDtoOutput = listaAssuntos.stream().map(this::converteParaDto).collect(Collectors.toList());

        return new ResponseEntity<>(listaAssuntoDtoOutput, OK);
    }

    public ResponseEntity<?> buscarAssuntoPeloId(Integer id) {
        Assunto assunto = assuntoRepository.findById(id).orElse(null);

        if (assunto == null) {
            return new ResponseEntity<>(String.format("Assunto não encontrado (id = %d)", id), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(assunto), OK);
    }

    public ResponseEntity<?> cadastrarAssunto(AssuntoDtoInput assuntoDtoInput) {
        if (assuntoDtoInput.getDescricao() == null || assuntoDtoInput.getDtCadastro() == null || assuntoDtoInput.getFlAtivo() == null) {
            return new ResponseEntity<>("Todos os campos devem ser preenchidos.", HttpStatus.BAD_REQUEST);
        }

        Assunto assunto = converteParaAssunto(assuntoDtoInput);
        assuntoRepository.save(assunto);

        return new ResponseEntity<>("Assunto cadastrado com sucesso", OK);
    }

}
