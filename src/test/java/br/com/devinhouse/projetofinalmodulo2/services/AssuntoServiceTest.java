package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class AssuntoServiceTest {

    @Mock
    private AssuntoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssuntoService service;

    @Test
    public void deveRetornarListaVaziaAoBuscarTodosOsAssuntos() {
        // given
        List<Assunto> listaVazia = Collections.emptyList();

        // when
        when(repository.findAll()).thenReturn(listaVazia);

        // then
        ResponseEntity<?> responseEntity = service.buscarTodosOsAssuntos();

        assertAll(
            () -> assertEquals(OK, responseEntity.getStatusCode()),
            () -> verify(repository, times(1)).findAll()
        );
    }

    @Test
    public void deveRetornarListaDeAssuntosAoBuscarTodosOsAssuntos() {
        // given
        Assunto assunto1 = new Assunto(1, "Assunto 1", LocalDate.parse("2020-01-01"), 's');
        Assunto assunto2 = new Assunto(2, "Assunto 2", LocalDate.parse("2020-01-01"), 's');

        AssuntoDtoOutput assuntoDto1 = new AssuntoDtoOutput(assunto1.getId(), assunto1.getDescricao(), assunto1.getDtCadastro().toString(), assunto1.getFlAtivo());
        AssuntoDtoOutput assuntoDto2 = new AssuntoDtoOutput(assunto2.getId(), assunto2.getDescricao(), assunto2.getDtCadastro().toString(), assunto2.getFlAtivo());

        //when
        when(modelMapper.map(assunto1, AssuntoDtoOutput.class)).thenReturn(assuntoDto1);
        when(modelMapper.map(assunto2, AssuntoDtoOutput.class)).thenReturn(assuntoDto2);

        when(repository.findAll()).thenReturn(Arrays.asList(assunto1, assunto2));

        // then
        ResponseEntity<?> responseEntity = service.buscarTodosOsAssuntos();
        List<AssuntoDtoOutput> lista = (List<AssuntoDtoOutput>) responseEntity.getBody();

        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertThat(lista, is(not(empty()))),
                () -> assertThat(lista.get(0), is(equalTo(assuntoDto1))),
                () -> assertThat(lista.get(1), is(equalTo(assuntoDto2))),
                () -> verify(repository, times(1)).findAll()
        );
    }

    @Test
    public void deveRetornarAssuntoAoBuscarIdValido() {
        // given
        Assunto assunto = new Assunto(1, "descricao", LocalDate.parse("2020-01-01"), 's');
        AssuntoDtoOutput assuntoDto = new AssuntoDtoOutput(assunto.getId(), assunto.getDescricao(), assunto.getDtCadastro().toString(), assunto.getFlAtivo());

        // when
        when(modelMapper.map(assunto, AssuntoDtoOutput.class)).thenReturn(assuntoDto);
        when(repository.findById(1)).thenReturn(Optional.of(assunto));

        // then
        ResponseEntity<?> responseEntity = service.buscarAssuntoPeloId(1);
        AssuntoDtoOutput responseBody = (AssuntoDtoOutput) responseEntity.getBody();

        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertThat(responseBody, is(equalTo(assuntoDto))),
                () -> verify(repository, times(1)).findById(1)
        );
    }

    @Test
    public void deveRetornarStatusNotFoundAoBuscarIdInvalido() {
        // given
        Optional<Assunto> assunto = Optional.empty();

        // when
        when(repository.findById(100)).thenReturn(assunto);

        // then
        ResponseEntity<?> responseEntity = service.buscarAssuntoPeloId(100);

        assertAll(
                () -> assertEquals(NOT_FOUND, responseEntity.getStatusCode()),
                () -> verify(repository, times(1)).findById(100)
        );
    }

    @Test
    public void deveRetornarStatusCreatedAoCadastrarAssuntoValido() {
        // given
        AssuntoDtoInput assuntoDto = new AssuntoDtoInput("descricao", "2020-01-01", 's');
        Assunto assunto = new Assunto(1, assuntoDto.getDescricao(), LocalDate.parse(assuntoDto.getDtCadastro()), assuntoDto.getFlAtivo());

        // when
        when(modelMapper.map(assuntoDto, Assunto.class)).thenReturn(assunto);
        when(repository.save(assunto)).thenReturn(assunto);

        // then
        ResponseEntity<?> responseEntity = service.cadastrarAssunto(assuntoDto);

        assertAll(
                () -> assertEquals(CREATED, responseEntity.getStatusCode()),
                () -> verify(repository, times(1)).save(assunto)
        );
    }

    @Test
    public void deveRetornarStatusBadRequestAoCadastrarAssuntoIncompleto() {
        // given
        AssuntoDtoInput assuntoDto = new AssuntoDtoInput();
        assuntoDto.setDescricao("descricao");
        assuntoDto.setFlAtivo('s');

        // then
        ResponseEntity<?> responseEntity = service.cadastrarAssunto(assuntoDto);

        assertAll(
                () -> assertEquals(BAD_REQUEST, responseEntity.getStatusCode()),
                () -> verify(repository, times(0)).save(any(Assunto.class))
        );
    }

    @Test
    public void deveRetornarStatusBadRequestAoCadastrarAssuntoComDataInvalida() {
        // given
        AssuntoDtoInput assuntoDto = new AssuntoDtoInput("descricao", "2020-02-31", 's');

        // then
        ResponseEntity<?> responseEntity = service.cadastrarAssunto(assuntoDto);

        assertAll(
                () -> assertEquals(BAD_REQUEST, responseEntity.getStatusCode()),
                () -> verify(repository, times(0)).save(any(Assunto.class))
        );
    }

    @Test
    public void deveRetornarStatusBadRequestAoCadastrarAssuntoComFlAtivoInvalido() {
        // given
        AssuntoDtoInput assuntoDto = new AssuntoDtoInput("descricao", "2020-01-01", 'y');

        // then
        ResponseEntity<?> responseEntity = service.cadastrarAssunto(assuntoDto);

        assertAll(
                () -> assertEquals(BAD_REQUEST, responseEntity.getStatusCode()),
                () -> verify(repository, times(0)).save(any(Assunto.class))
        );
    }

}
