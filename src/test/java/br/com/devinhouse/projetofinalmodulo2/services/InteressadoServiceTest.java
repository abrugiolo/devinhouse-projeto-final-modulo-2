package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.exceptions.*;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class InteressadoServiceTest {

    @Mock
    private InteressadoRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InteressadoService service;

    private static MockedStatic<ValidacaoCampos> validacaoCampos;

    @BeforeAll
    public static void setup() {
        validacaoCampos = mockStatic(ValidacaoCampos.class);
    }

    @AfterAll
    public static void tearDown() {
        validacaoCampos.close();
    }

    @Test
    public void deveRetornarMensagemNenhumEncontradoAoBuscarTodosOsInteressados() {
        // given
        List<Interessado> listaVazia = Collections.emptyList();

        // when
        when(repository.findAll()).thenReturn(listaVazia);

        // then
        ResponseEntity<?> responseEntity = service.buscarTodosOsInteressados();

        assertAll(
            () -> assertEquals(OK, responseEntity.getStatusCode()),
            () -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
            () -> verify(repository, times(1)).findAll()
        );
    }

    @Test
    public void deveRetornarListaAoBuscarTodosOsInteressados() {
        // given
        Interessado fulano = new Interessado(1, "fulano", "12345654321", LocalDate.parse("1950-01-01"), 's');
        Interessado beltrano = new Interessado();
        beltrano.setId(2);
        beltrano.setNmInteressado("beltrano");
        beltrano.setNuIdentificacao("45678987654");
        beltrano.setDtNascimento(LocalDate.parse("1955-01-01"));
        beltrano.setFlAtivo('s');

        InteressadoDtoOutput fulanoDto = new InteressadoDtoOutput();
        fulanoDto.setId(fulano.getId());
        fulanoDto.setNmInteressado(fulano.getNmInteressado());
        fulanoDto.setNuIdentificacao(fulano.getNuIdentificacao());
        fulanoDto.setDtNascimento(fulano.getDtNascimento().toString());
        fulanoDto.setFlAtivo(fulano.getFlAtivo());

        InteressadoDtoOutput beltranoDto = new InteressadoDtoOutput();
        beltranoDto.setId(beltrano.getId());
        beltranoDto.setNmInteressado(beltrano.getNmInteressado());
        beltranoDto.setNuIdentificacao(beltrano.getNuIdentificacao());
        beltranoDto.setDtNascimento(beltrano.getDtNascimento().toString());
        beltranoDto.setFlAtivo(beltrano.getFlAtivo());

        // when
        when(modelMapper.map(fulano, InteressadoDtoOutput.class)).thenReturn(fulanoDto);
        when(modelMapper.map(beltrano, InteressadoDtoOutput.class)).thenReturn(beltranoDto);

        when(repository.findAll()).thenReturn(Arrays.asList(fulano, beltrano));

        // then
        ResponseEntity<?> responseEntity = service.buscarTodosOsInteressados();
        List<InteressadoDtoOutput> lista = (List<InteressadoDtoOutput>) responseEntity.getBody();

        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertThat(lista, is(not(empty()))),
                () -> assertThat(lista.get(0), is(equalTo(fulanoDto))),
                () -> assertThat(lista.get(1), is(equalTo(beltranoDto))),
                () -> verify(repository, times(1)).findAll()
        );
    }

    @Test
    public void deveRetornarInteressadoAoBuscarIdValido() {
        // given
        Interessado interessado = new Interessado(1, "fulano", "12345654321", LocalDate.parse("1950-01-01"), 's');
        InteressadoDtoOutput interessadoDto = new InteressadoDtoOutput();
        interessadoDto.setId(interessado.getId());
        interessadoDto.setNmInteressado(interessado.getNmInteressado());
        interessadoDto.setNuIdentificacao(interessado.getNuIdentificacao());
        interessadoDto.setDtNascimento(interessado.getDtNascimento().toString());
        interessadoDto.setFlAtivo(interessado.getFlAtivo());

        // when
        when(modelMapper.map(interessado, InteressadoDtoOutput.class)).thenReturn(interessadoDto);
        when(repository.findById(1)).thenReturn(Optional.of(interessado));

        // then
        ResponseEntity<?> responseEntity = service.buscarInteressadoPeloId(1);

        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertThat(responseEntity.getBody(), is(equalTo(interessadoDto))),
                () -> verify(repository, times(1)).findById(1)
        );
    }

    @Test
    public void deveLancarExcecaoAoBuscarIdInexistente() {
        assertThrows(NotFoundException.class, () -> {
            // given
            Optional<Interessado> interessado = Optional.empty();

            // when
            when(repository.findById(100)).thenReturn(interessado);

            // then
            service.buscarInteressadoPeloId(100);
        });
    }

    @Test
    public void deveRetornarInteressadoAoBuscarNuIdentificacaoValido() {
        // given
        Interessado interessado = new Interessado(1, "fulano", "12345654321", LocalDate.parse("1950-01-01"), 's');
        InteressadoDtoOutput interessadoDto = new InteressadoDtoOutput();
        interessadoDto.setId(interessado.getId());
        interessadoDto.setNmInteressado(interessado.getNmInteressado());
        interessadoDto.setNuIdentificacao(interessado.getNuIdentificacao());
        interessadoDto.setDtNascimento(interessado.getDtNascimento().toString());
        interessadoDto.setFlAtivo(interessado.getFlAtivo());

        // when
        when(modelMapper.map(interessado, InteressadoDtoOutput.class)).thenReturn(interessadoDto);
        when(repository.findByNuIdentificacao("12345654321")).thenReturn(Optional.of(interessado));

        // then
        ResponseEntity<?> responseEntity = service.buscarInteressadoPeloNumeroDeIdentificacao("12345654321");

        assertAll(
                () -> assertEquals(OK, responseEntity.getStatusCode()),
                () -> assertThat(responseEntity.getBody(), is(equalTo(interessadoDto))),
                () -> verify(repository, times(1)).findByNuIdentificacao("12345654321")
        );
    }

    @Test
    public void deveLancarExcecaoAoBuscarNuIdentificacaoInexistente() {
        assertThrows(NotFoundException.class, () -> {
            // given
            Optional<Interessado> interessado = Optional.empty();

            // when
            when(repository.findByNuIdentificacao("123456")).thenReturn(interessado);

            // then
            service.buscarInteressadoPeloNumeroDeIdentificacao("123456");
        });
    }

    @Test
    public void deveRetornarCreatedAoCadastrarInteressadoValido() {
        // given
        InteressadoDtoInput interessadoDto = new InteressadoDtoInput();
        interessadoDto.setNuIdentificacao("12345654321");
        Interessado interessado = new Interessado();
        interessadoDto.setNuIdentificacao(interessadoDto.getNuIdentificacao());

        // when
        when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(true);
        when(ValidacaoCampos.validarData(interessadoDto.getDtNascimento())).thenReturn(true);
        when(ValidacaoCampos.validarFlAtivo(interessadoDto.getFlAtivo())).thenReturn(true);

        when(repository.existsByNuIdentificacao(interessadoDto.getNuIdentificacao())).thenReturn(false);

        when(modelMapper.map(interessadoDto, Interessado.class)).thenReturn(interessado);
        when(repository.save(interessado)).thenReturn(interessado);

        // then
        ResponseEntity<?> responseEntity = service.cadastrarInteressado(interessadoDto);

        assertAll(
                () -> assertEquals(CREATED, responseEntity.getStatusCode()),
                () -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
                () -> verify(repository, times(1)).save(interessado)
        );
    }

    @Test
    public void deveLancarExcecaoAoCadastrarInteressadoIncompleto() {
        assertThrows(CampoVazioException.class, () -> {
            // given
            InteressadoDtoInput interessadoDto = new InteressadoDtoInput();

            // when
            when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(false);

            // then
            service.cadastrarInteressado(interessadoDto);
        });
    }

    @Test
    public void deveLancarExcecaoAoCadastrarInteressadoComDataNascimentoInvalida() {
        assertThrows(DataInvalidaException.class, () -> {
            // given
            InteressadoDtoInput interessadoDto = new InteressadoDtoInput();

            // when
            when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(true);
            when(ValidacaoCampos.validarData(interessadoDto.getDtNascimento())).thenReturn(false);

            // then
            service.cadastrarInteressado(interessadoDto);
        });
    }

    @Test
    public void deveLancarExcecaoAoCadastrarInteressadoComFlAtivoInvalido() {
        assertThrows(FlAtivoInvalidoException.class, () -> {
            // given
            InteressadoDtoInput interessadoDto = new InteressadoDtoInput();

            // when
            when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(true);
            when(ValidacaoCampos.validarData(interessadoDto.getDtNascimento())).thenReturn(true);
            when(ValidacaoCampos.validarFlAtivo(interessadoDto.getFlAtivo())).thenReturn(false);

            // then
            service.cadastrarInteressado(interessadoDto);
        });
    }

    @Test
    public void deveLancarExcecaoAoCadastrarInteressadoComNuIdentificacaoInvalido() {
        assertThrows(NuIdentificacaoInvalidoException.class, () -> {
            // given
            InteressadoDtoInput interessadoDto = new InteressadoDtoInput();
            interessadoDto.setNuIdentificacao("123456");

            // when
            when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(true);
            when(ValidacaoCampos.validarData(interessadoDto.getDtNascimento())).thenReturn(true);
            when(ValidacaoCampos.validarFlAtivo(interessadoDto.getFlAtivo())).thenReturn(true);

            // when/then
            service.cadastrarInteressado(interessadoDto);
        });
    }

    @Test
    public void deveLancarExcecaoAoCadastrarInteressadoComNuIdentificacaoExistente() {
        assertThrows(NuIdentificacaoJaExisteException.class, () -> {
            // given
            InteressadoDtoInput interessadoDto = new InteressadoDtoInput();
            interessadoDto.setNuIdentificacao("12345654321");

            // when
            when(ValidacaoCampos.validarCamposPreenchidos(interessadoDto)).thenReturn(true);
            when(ValidacaoCampos.validarData(interessadoDto.getDtNascimento())).thenReturn(true);
            when(ValidacaoCampos.validarFlAtivo(interessadoDto.getFlAtivo())).thenReturn(true);
            when(repository.existsByNuIdentificacao("12345654321")).thenReturn(true);

            // then
            service.cadastrarInteressado(interessadoDto);
        });
    }

}
