package br.com.devinhouse.projetofinalmodulo2.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.hamcrest.Matchers.*;
import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
import br.com.devinhouse.projetofinalmodulo2.exceptions.InteressadoInativoException;
import br.com.devinhouse.projetofinalmodulo2.exceptions.NotFoundException;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ValidacaoCampos;

@ExtendWith(MockitoExtension.class)
class ProcessoServiceTest {

	@Mock
	private ProcessoRepository repositoryProcesso;

	@Mock
	private InteressadoRepository repositoryInteressado;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private ValidacaoCampos validacaoCampos;

	@InjectMocks
	private ProcessoService service;

	@Test
	void deveRetornarProcessoInformandoInteressado() {

		Interessado interessado = new Interessado(1, "Joao", "12345678901", LocalDate.parse("2000-05-19"), 's');
		ProcessoDtoOutput processoDtoOutput = new ProcessoDtoOutput();
		Processo processo = new Processo();

		when(repositoryInteressado.findById(1)).thenReturn(Optional.of(interessado));
		when(repositoryProcesso.findByCdInteressado(interessado)).thenReturn(Arrays.asList(processo));
		when(modelMapper.map(processo, ProcessoDtoOutput.class)).thenReturn(processoDtoOutput);

		ResponseEntity<?> responseEntity = service.buscarProcessosPorInteressado(1);
		List<AssuntoDtoOutput> lista = (List<AssuntoDtoOutput>) responseEntity.getBody();

		assertAll(() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
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

		assertAll(() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
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

		assertAll(() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
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

		assertAll(() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
				() -> assertEquals(processoAtual.getClass(), ProcessoDtoOutput.class));
	}

	@Test
	void deveRetornarExceptionParaNumeroDeProcessoNaoExistente() {
		when(repositoryProcesso.findByNuProcesso(1)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarProcessoPeloNumeroProcesso(1)).isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Nenhum processo encontrado com número '1'.");
	}

	@Test
	void testAtualizarProcesso() {
		fail("Not yet implemented");
	}

	@Test
	void testDeletarProcesso() {
		fail("Not yet implemented");
	}

}
