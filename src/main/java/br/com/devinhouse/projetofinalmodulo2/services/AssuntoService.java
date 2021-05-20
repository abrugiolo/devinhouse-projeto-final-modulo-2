package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDto;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Assunto converteParaAssunto(AssuntoDto assuntoDto) {
        return modelMapper.map(assuntoDto, Assunto.class);
    }

    private AssuntoDto converteParaDto(Assunto assunto) {
        return modelMapper.map(assunto, AssuntoDto.class);
    }

    public ResponseEntity<?> buscarTodosOsAssuntos() {
        List<Assunto> listaAssuntos = assuntoRepository.findAll();

        if (listaAssuntos.isEmpty()) {
            return new ResponseEntity<>("Não existem assuntos cadastrados", OK);
        }

        List<AssuntoDto> listaAssuntoDto = listaAssuntos.stream().map(this::converteParaDto).collect(Collectors.toList());

        return new ResponseEntity<>(listaAssuntoDto, OK);
    }

    public ResponseEntity<?> buscarAssuntoPeloId(Integer id) {
        Assunto assunto = assuntoRepository.findById(id).orElse(null);

        if (assunto == null) {
            return new ResponseEntity<>(String.format("Assunto não encontrado (id = %d)", id), NOT_FOUND);
        }

        return new ResponseEntity<>(converteParaDto(assunto), OK);
    }

    public ResponseEntity<?> cadastrarAssunto(AssuntoDto assuntoDto) {
        Assunto assunto = converteParaAssunto(assuntoDto);

        assuntoRepository.save(assunto);

        return new ResponseEntity<>("Assunto cadastrado com sucesso", OK);
    }

}
