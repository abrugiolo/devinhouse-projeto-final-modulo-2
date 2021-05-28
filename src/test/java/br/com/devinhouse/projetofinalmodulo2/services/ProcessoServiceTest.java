package br.com.devinhouse.projetofinalmodulo2.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
import br.com.devinhouse.projetofinalmodulo2.exceptions.AssuntoInativoException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.CampoVazioException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.ChaveProcessoJaExisteException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.InteressadoInativoException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.NotFoundException;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;

@ExtendWith(MockitoExtension.class)
class ProcessoServiceTest {

	@Mock
	private ProcessoRepository repositoryProcesso;

	@Mock
	private InteressadoRepository repositoryInteressado;

	@Mock
	private AssuntoRepository repositoryAssunto;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ProcessoService service;
	
	private static MockedStatic<ValidacaoCampos> valida;

	@AfterAll
	 public static void tearDown() {
        valida.close();
    }
	

    @BeforeAll
    public static void setup() {
        valida = mockStatic(ValidacaoCampos.class);
    }
	
	@Test
	void deveRetornarProcessoInformandoInteressado() {

		Interessado interessado = new Interessado(1, "Joao", "12345678901", LocalDate.parse("2000-05-19"), 's');
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();
		processoDtoOutput.setCdAssunto(null);
		processoDtoOutput.setCdInteressado(null);
		processoDtoOutput.setChaveProcesso(null);
		processoDtoOutput.setDescricao(null);
		processoDtoOutput.setId(null);
		processoDtoOutput.setNuAno(null);
		processoDtoOutput.setSgOrgaoSetor(null);
		Processo processo = new Processo();

		when(repositoryInteressado.findById(1)).thenReturn(Optional.of(interessado));
		when(repositoryProcesso.findByCdInteressado(interessado)).thenReturn(Arrays.asList(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarProcessosPorInteressado(1);
		List<AssuntoDtoOutput> lista = (List<AssuntoDtoOutput>) responseEntity.getBody();

		assertAll(() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(lista, is(not(empty()))));
	}

	@Test
	void deveRetornarInteressadoVazio() {
		when(repositoryInteressado.findById(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessosPorInteressado(1)).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado para o interessado informado.");
	}

	@Test
	void deveRetornarExceptionParaInteressadoInativo() {
		Interessado interessado = new Interessado(1, "Joao", "12345678901", LocalDate.parse("2000-05-19"), 'n');

		when(repositoryInteressado.findById(1)).thenReturn(Optional.of(interessado));

		assertThatThrownBy(() -> service.buscarProcessosPorInteressado(1))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível efetuar a busca: Interessado inativo.");
	}

	@Test
	void deveBuscarTodosOsProcessos() {
		List<Processo> processoList = new ArrayList<Processo>();
		processoList.add(new Processo());
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();

		when(repositoryProcesso.findAll()).thenReturn(processoList);
		when(modelMapper.map(processoList.get(0), ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarTodosOsProcessos();
		List<ProcessoDtoOutput> lista = (List<ProcessoDtoOutput>) responseEntity.getBody();

		assertAll(() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(lista, is(not(empty()))));
	}

	@Test
	void deveRetornarExceptionParaNenhumProcesso() {
		List<Processo> processoList = new ArrayList<Processo>();

		when(repositoryProcesso.findAll()).thenReturn(processoList);

		assertThatThrownBy(() -> service.buscarTodosOsProcessos()).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Não existem processos cadastrados.");
	}

	@Test
	void deveRetornarProcessoPeloId() {
		Processo processo = new Processo();
		when(repositoryProcesso.findById(1)).thenReturn(Optional.of(processo));

		ResponseEntity<?> responseEntity = service.buscarProcessoPeloId(1);
		Processo processoAtual = (Processo) responseEntity.getBody();

		assertAll(() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(processoAtual, null));
	}

	@Test
	void deveRetornarExceptionParaProcessoNãoExistente() {
		when(repositoryProcesso.findById(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessoPeloId(1)).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado com id '1'.");
	}

	@Test
	void deveRetornarProcessoPeloNumeroDeProcesso() {
		Processo processo = new Processo();
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();

		when(repositoryProcesso.findByNuProcesso(1)).thenReturn(Optional.of(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarProcessoPeloNumeroProcesso(1);
		ProcessoDtoOutput processoAtual = (ProcessoDtoOutput) responseEntity.getBody();

		assertAll(() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(processoAtual.getClass(), ProcessoDtoOutput.class));
	}

	@Test
	void deveRetornarExceptionParaNumeroDeProcessoNaoExistente() {
		when(repositoryProcesso.findByNuProcesso(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessoPeloNumeroProcesso(1)).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado com número '1'.");
	}

	@Test
	void deveCadastrarNovoProcesso() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();
		Processo processo = new Processo();

		processoDtoInput.setChaveProcesso("SOFT 1/2021");
		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");

		valida.when(() -> ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(modelMapper.map(processoDtoInput, Processo.class)).thenReturn(processo);
		when(repositoryProcesso.save(processo)).thenReturn(processo);

		ResponseEntity<?> responseEntity = service.cadastrarProcesso(processoDtoInput);
		verify(repositoryProcesso).save(processo);

		assertAll(() -> assertEquals(CREATED, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))));
	}
	
	@Test
	void deveCadastrarNovoProcessoNuProcessoMaiorQueUm() {
		List<Processo> processoList = new ArrayList<Processo>();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();
		processoList.add(new Processo(1, "SOFT", 1, "2021", "SOFT 1/2021", "Descricao", assunto, interessado));
		Processo processo = new Processo();

		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setSgOrgaoSetor("SOFT");

		valida.when(() -> ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(repositoryProcesso.findAll()).thenReturn(processoList);
		when(repositoryProcesso.existsByChaveProcesso(anyString())).thenReturn(false);
		valida.when(() -> ValidacaoCampos.validadorInteressadoInativo(repositoryInteressado, interessado)).thenReturn(false);
		valida.when(() -> ValidacaoCampos.validadorAssuntoInativo(repositoryAssunto, assunto)).thenReturn(false);
		when(modelMapper.map(processoDtoInput, Processo.class)).thenReturn(processo);
		when(repositoryProcesso.save(processo)).thenReturn(processo);

		ResponseEntity<?> responseEntity = service.cadastrarProcesso(processoDtoInput);
        
		assertAll(() -> assertEquals(CREATED, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
				() -> assertEquals(Integer.valueOf(2), processoDtoInput.getNuProcesso()));
	}

	@Test
	void deveLancarExceptionParaPreencherTodosOsCampos() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput)).isInstanceOf(CampoVazioException.class)
				.hasMessageContaining("Todos os campos devem estar preenchidos.");
	}

	@Test
	void deveRetornarExceptionParaChaveProcessoJaCadastrados() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();

		processoDtoInput.setChaveProcesso("SOFT 1/2021");
		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");

		valida.when(() -> ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(repositoryProcesso.existsByChaveProcesso(processoDtoInput.getChaveProcesso())).thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(ChaveProcessoJaExisteException.class)
				.hasMessageContaining("Já existe um processo cadastrado com a chave 'SOFT 1/2021'.");
	}

	@Test
	void deveRetornarExceptionParaInteressadoInativoOuInexistente() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		interessado.setFlAtivo('n');
		Assunto assunto = new Assunto();

		processoDtoInput.setChaveProcesso("SOFT 1/2021");
		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");

		valida.when(() -> ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		valida.when(() -> ValidacaoCampos.validadorInteressadoInativo(repositoryInteressado,
				processoDtoInput.getCdInteressado())).thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.");
	}

	@Test
	void deveRetornarExceptionParaAssuntoInativoOuInexistente() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();
		assunto.setFlAtivo('n');

		processoDtoInput.setChaveProcesso("SOFT 1/2021");
		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");

		valida.when(() -> ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		valida.when(() -> ValidacaoCampos.validadorAssuntoInativo(repositoryAssunto, processoDtoInput.getCdAssunto()))
				.thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(AssuntoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.");
	}

	@Test
	void deveAtualizarProcesso() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();

		processoDtoInput.setChaveProcesso("SOFT 1/2021");
		processoDtoInput.setCdAssunto(assunto);
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setDescricao("Descricao");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");

		when(repositoryProcesso.findById(1)).thenReturn(Optional.of(processo));

		ResponseEntity<?> responseEntity = service.atualizarProcesso(1, processoDtoInput);

		ArgumentCaptor<Processo> processoArgumentCaptor = ArgumentCaptor.forClass(Processo.class);
		verify(repositoryProcesso).save(processoArgumentCaptor.capture());

		Processo capturedProcesso = processoArgumentCaptor.getValue();
		assertThat(capturedProcesso.getCdAssunto(), is(processoDtoInput.getCdAssunto()));
		assertThat(capturedProcesso.getChaveProcesso(), is(processoDtoInput.getChaveProcesso()));
		assertThat(capturedProcesso.getCdInteressado(), is(processoDtoInput.getCdInteressado()));
		assertThat(capturedProcesso.getDescricao(), is(processoDtoInput.getDescricao()));

		assertAll(() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))));
	}

	@Test
	void deveLancarExceptionParaChaveExistente() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setNuProcesso(1);
		processoDtoInput.setSgOrgaoSetor("SOFT");
		processo.setChaveProcesso("SOFT 1/2021");

		when(repositoryProcesso.findById(1)).thenReturn(Optional.of(processo));
		when(repositoryProcesso.existsByChaveProcesso(processo.getChaveProcesso())).thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(ChaveProcessoJaExisteException.class)
				.hasMessageContaining("Chave do processo já existe no sistema.");
	}

	@Test
	void deveLancarExceptionParaInteressadoInativo() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		interessado.setFlAtivo('n');
		processoDtoInput.setCdInteressado(interessado);

		when(repositoryProcesso.findById(1)).thenReturn(Optional.of(processo));
		valida.when(() -> ValidacaoCampos.validadorInteressadoInativo(repositoryInteressado,
				processoDtoInput.getCdInteressado())).thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.");
	}

	@Test
	void deveLancarExceptionParaAssuntoInativo() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Assunto assunto = new Assunto();
		assunto.setFlAtivo('n');
		processoDtoInput.setCdAssunto(assunto);

		when(repositoryProcesso.findById(1)).thenReturn(Optional.of(processo));
		valida.when(() -> ValidacaoCampos.validadorAssuntoInativo(repositoryAssunto, processoDtoInput.getCdAssunto()))
				.thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(AssuntoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.");
	}
  
  @Test
  	public void deveRetornarOkAoDeletarProcessoExistente() {

		when(repositoryProcesso.existsById(1)).thenReturn(true);

		ResponseEntity responseEntity = service.deletarProcesso(1);

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> verify(repositoryProcesso, times(1)).deleteById(1)
		);
	}

	@Test
	public void deveLancarExcecaoAoDeletarProcessoInexistente() {

		assertThrows(NotFoundException.class, () -> {

			when(repositoryProcesso.existsById(1)).thenReturn(false);

			service.deletarProcesso(1);
		});

		verify(repositoryProcesso, times(0)).deleteById(1);
  }
}
