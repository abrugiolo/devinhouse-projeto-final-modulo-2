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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
	private ProcessoRepository processoRepository;

	@Mock
	private InteressadoRepository interessadoRepository;

	@Mock
	private AssuntoRepository assuntoRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ProcessoService service;
	
	private static MockedStatic<ValidacaoCampos> valida;

	@BeforeAll
	public static void setup() {
		valida = mockStatic(ValidacaoCampos.class);
	}

	@AfterAll
	 public static void tearDown() {
        valida.close();
    }

	@Test
	void deveBuscarTodosOsProcessos() {
		Processo processo = new Processo();
		List<Processo> processoList = Collections.singletonList(processo);
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();

		when(processoRepository.findAll()).thenReturn(processoList);
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarTodosOsProcessos();
		List<ProcessoDtoOutput> listaDto = (List<ProcessoDtoOutput>) responseEntity.getBody();

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(listaDto, is(not(empty()))),
				() -> assertEquals(1, listaDto.size()),
				() -> assertEquals(processoDtoOutput, listaDto.get(0))
		);
	}

	@Test
	void deveRetornarMensagemNenhumEncontradoAoBuscarPorTodosOsProcessos() {
		List<Processo> processoList = new ArrayList<>();

		when(processoRepository.findAll()).thenReturn(processoList);

		ResponseEntity responseEntity = service.buscarTodosOsProcessos();

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(responseEntity.getBody(), "Não existem processos cadastrados.")
		);
	}

	@Test
	void deveRetornarProcessoAoBuscarPeloId() {
		Processo processo = new Processo();
		ProcessoDtoOutput processoDto = new ProcessoDtoOutput();

		when(processoRepository.findById(1)).thenReturn(Optional.of(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDto);

		ResponseEntity<?> responseEntity = service.buscarProcessoPeloId(1);

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(processoDto, responseEntity.getBody())
		);
	}

	@Test
	void deveLancarExcecaoAoBuscarPorIdInexistente() {
		when(processoRepository.findById(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessoPeloId(1))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado com id '1'.");
	}

	@Test
	void deveRetornarProcessoAoBuscarPeloNumeroDeProcesso() {
		Processo processo = new Processo();
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();

		when(processoRepository.findByNuProcesso(1)).thenReturn(Optional.of(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarProcessoPeloNumeroProcesso(1);

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(processoDtoOutput, responseEntity.getBody())
		);
	}

	@Test
	void deveLancarExcecaoAoBuscarPorNumeroDeProcessoInexistente() {
		when(processoRepository.findByNuProcesso(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessoPeloNumeroProcesso(1))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado com número '1'.");
	}

	@Test
	void deveRetornarListaDeProcessosAoBuscarPorInteressado() {
		Assunto assunto = new Assunto();
		Interessado interessado = new Interessado();
		interessado.setFlAtivo('s');
		Processo processo = new Processo(1, "SOFT", 1, "2021", "SOFT 1/2021", "descricao", assunto, interessado);
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();

		when(interessadoRepository.findById(1)).thenReturn(Optional.of((interessado)));
		when(processoRepository.findByCdInteressado(interessado)).thenReturn(Collections.singletonList(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarProcessosPorInteressado(1);
		List<ProcessoDtoOutput> lista = (List<ProcessoDtoOutput>) responseEntity.getBody();

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(lista, is(not(empty()))),
				() -> assertEquals(1, lista.size()),
				() -> assertThat(lista.get(0), is(equalTo(processoDtoOutput)))
		);
	}

	@Test
	void deveLancarExcecaoAoBuscarPorInteressadoInexistente() {
		when(interessadoRepository.findById(1)).thenReturn(Optional.empty());

		assertThatThrownBy(
				() -> service.buscarProcessosPorInteressado(1)).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado para o interessado informado.");
	}

	@Test
	void deveLancarExcecaoAoBuscarPorInteressadoInativo() {
		Interessado interessado = new Interessado();
		interessado.setFlAtivo('n');

		when(interessadoRepository.findById(1)).thenReturn(Optional.of(interessado));

		assertThatThrownBy(
				() -> service.buscarProcessosPorInteressado(1))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível efetuar a busca: Interessado inativo.");
	}

	@Test
	void deveCadastrarNovoProcessoComSucesso() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setSgOrgaoSetor("SOFT");
		processoDtoInput.setNuAno("2021");
		Interessado interessado = new Interessado();
		processoDtoInput.setCdInteressado(interessado);
		Assunto assunto = new Assunto();
		processoDtoInput.setCdAssunto(assunto);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(false);

		when(processoRepository.findAll()).thenReturn(Collections.emptyList());

		when(processoRepository.existsByChaveProcesso(anyString())).thenReturn(false);

		when(modelMapper.map(processoDtoInput, Processo.class)).thenReturn(processo);
		when(processoRepository.save(processo)).thenReturn(any(Processo.class));

		ResponseEntity<?> responseEntity = service.cadastrarProcesso(processoDtoInput);

		assertAll(
				() -> assertEquals(CREATED, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
				() -> verify(processoRepository, times(1)).save(processo)
		);
	}
	
	@Test
	void deveCadastrarNovoProcessoNuProcessoMaiorQueUm() {
		Processo processo = new Processo();

		Processo unicoProcessoNaLista = new Processo();
		unicoProcessoNaLista.setNuProcesso(1);
		List<Processo> processoList = Collections.singletonList(unicoProcessoNaLista);

		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setSgOrgaoSetor("SOFT");
		Interessado interessado = new Interessado();
		processoDtoInput.setCdInteressado(interessado);
		Assunto assunto = new Assunto();
		processoDtoInput.setCdAssunto(assunto);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(false);

		when(processoRepository.findAll()).thenReturn(processoList);

		when(processoRepository.existsByChaveProcesso(anyString())).thenReturn(false);

		when(modelMapper.map(processoDtoInput, Processo.class)).thenReturn(processo);
		when(processoRepository.save(processo)).thenReturn(processo);

		ResponseEntity<?> responseEntity = service.cadastrarProcesso(processoDtoInput);
        
		assertAll(
				() -> assertEquals(CREATED, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
				() -> assertEquals(unicoProcessoNaLista.getNuProcesso()+1, processoDtoInput.getNuProcesso())
		);
	}

	@Test
	void deveLancarExcecaoParaPreencherTodosOsCampos() {
		assertThatThrownBy(() -> service.cadastrarProcesso(new ProcessoDtoInput()))
				.isInstanceOf(CampoVazioException.class)
				.hasMessageContaining("Todos os campos devem estar preenchidos.");
	}

	@Test
	void deveLancarExcecaoAoCadastrarProcessoComChaveExistente() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setSgOrgaoSetor("SOFT");
		processoDtoInput.setNuAno("2021");
		Interessado interessado = new Interessado();
		processoDtoInput.setCdInteressado(interessado);
		Assunto assunto = new Assunto();
		processoDtoInput.setCdAssunto(assunto);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(false);

		when(processoRepository.existsByChaveProcesso(anyString())).thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(ChaveProcessoJaExisteException.class)
				.hasMessageContaining("Já existe um processo cadastrado com a chave '%s'.", processoDtoInput.getChaveProcesso());
	}

	@Test
	void deveLancarExcecaoAoCadastrarComInteressadoInativoOuInexistente() {
		Interessado interessado = new Interessado();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setCdInteressado(interessado);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Interessado inativo ou inexistente.");

	}

	@Test
	void deveRetornarExceptionParaAssuntoInativoOuInexistente() {
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		processoDtoInput.setCdInteressado(interessado);
		Assunto assunto = new Assunto();
		processoDtoInput.setCdAssunto(assunto);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(true);

		assertThatThrownBy(() -> service.cadastrarProcesso(processoDtoInput))
				.isInstanceOf(AssuntoInativoException.class)
				.hasMessageContaining("Não foi possível cadastrar o processo: Assunto inativo ou inexistente.");
	}

	@Test
	void deveAtualizarProcessoComSucesso() {
		Processo processo = new Processo();
		processo.setSgOrgaoSetor("qwer");
		processo.setNuAno("2020");
		processo.setNuProcesso(1);

		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		Interessado interessado = new Interessado();
		Assunto assunto = new Assunto();
		processoDtoInput.setSgOrgaoSetor("SOFT");
		processoDtoInput.setNuAno("2021");
		processoDtoInput.setDescricao("Descricao Processo");
		processoDtoInput.setCdInteressado(interessado);
		processoDtoInput.setCdAssunto(assunto);

		when(ValidacaoCampos.validarCamposPreenchidos(processoDtoInput)).thenReturn(true);
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(false);

		when(processoRepository.findById(1)).thenReturn(Optional.of(processo));

		ResponseEntity<?> responseEntity = service.atualizarProcesso(1, processoDtoInput);

		ArgumentCaptor<Processo> processoArgumentCaptor = ArgumentCaptor.forClass(Processo.class);
		verify(processoRepository).save(processoArgumentCaptor.capture());

		Processo capturedProcesso = processoArgumentCaptor.getValue();

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertThat(responseEntity.getBody(), is(instanceOf(String.class))),
				() -> assertThat(capturedProcesso.getDescricao(), is(processoDtoInput.getDescricao())),
				() -> assertThat(capturedProcesso.getNuAno(), is(processoDtoInput.getNuAno())),
				() -> assertThat(capturedProcesso.getSgOrgaoSetor(), is(processoDtoInput.getSgOrgaoSetor())),
				() -> assertThat(capturedProcesso.getCdAssunto(), is(processoDtoInput.getCdAssunto())),
				() -> assertThat(capturedProcesso.getCdInteressado(), is(processoDtoInput.getCdInteressado())),
				() -> assertThat(capturedProcesso.getChaveProcesso(), is(processo.getChaveProcesso()))
		);
	}

	@Test
	void deveLancarExcecaoAoAtualizarProcessoComChaveExistente() {
		Processo processo = new Processo();
		processo.setNuProcesso(1);
		processo.setCdAssunto(new Assunto());
		processo.setCdInteressado(new Interessado());
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setSgOrgaoSetor("SOFT");
		processoDtoInput.setNuAno("2021");

		when(processoRepository.findById(1)).thenReturn(Optional.of(processo));

		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processo.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processo.getCdAssunto())).thenReturn(false);

		when(processoRepository.existsByChaveProcesso("SOFT 1/2021")).thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(ChaveProcessoJaExisteException.class)
				.hasMessageContaining("Chave do processo já existe no sistema.");
	}

	@Test
	void deveLancarExcecaoAoAtualizarProcessoComInteressadoInativo() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setCdInteressado(new Interessado());

		when(processoRepository.findById(1)).thenReturn(Optional.of(processo));
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(InteressadoInativoException.class)
				.hasMessageContaining("Não foi possível atualizar o processo: Interessado inativo ou inexistente.");
	}

	@Test
	void deveLancarExcecaoAoAtualizarProcessoComAssuntoInativo() {
		Processo processo = new Processo();
		ProcessoDtoInput processoDtoInput = new ProcessoDtoInput();
		processoDtoInput.setCdAssunto(new Assunto());
		processoDtoInput.setCdInteressado(new Interessado());

		when(processoRepository.findById(1)).thenReturn(Optional.of(processo));
		when(ValidacaoCampos.validadorInteressadoInativo(interessadoRepository, processoDtoInput.getCdInteressado())).thenReturn(false);
		when(ValidacaoCampos.validadorAssuntoInativo(assuntoRepository, processoDtoInput.getCdAssunto())).thenReturn(true);

		assertThatThrownBy(() -> service.atualizarProcesso(1, processoDtoInput))
				.isInstanceOf(AssuntoInativoException.class)
				.hasMessageContaining("Não foi possível atualizar o processo: Assunto inativo ou inexistente.");
	}
  
  @Test
  	public void deveDeletarProcessoComSucesso() {

		when(processoRepository.existsById(1)).thenReturn(true);

		ResponseEntity responseEntity = service.deletarProcesso(1);

		assertAll(
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> verify(processoRepository, times(1)).deleteById(1)
		);
	}

	@Test
	public void deveLancarExcecaoAoDeletarProcessoInexistente() {

		assertAll(
			() -> assertThrows(NotFoundException.class, () -> {
				when(processoRepository.existsById(1)).thenReturn(false);
				service.deletarProcesso(1);
			}),
			() -> verify(processoRepository, times(0)).deleteById(1)
		);
  }
}
