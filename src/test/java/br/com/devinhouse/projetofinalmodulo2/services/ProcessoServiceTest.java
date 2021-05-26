package br.com.devinhouse.projetofinalmodulo2.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;

@ExtendWith(MockitoExtension.class)
class ProcessoServiceTest {

	@Mock
	private ProcessoRepository repositoryProcesso;

	@Mock
	private InteressadoRepository repositoryInteressado;

	@Mock
	private ModelMapper modelMapper;

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
	
		assertAll(
				() -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
				() -> assertThat(lista, is(not(empty())))
		);
	}

	@Test
	void deveRetornarInteressadoVazio() {

	}

	@Test
	void deveRetornarInteressadoInativo() {

	}

	@Test
	void testBuscarTodosOsProcessos() {
		fail("Not yet implemented");
	}

	@Test
	void testBuscarProcessoPeloId() {
		fail("Not yet implemented");
	}

	@Test
	void testBuscarProcessoPeloNumeroProcesso() {
		fail("Not yet implemented");
	}

	@Test
	void testCadastrarProcesso() {
		fail("Not yet implemented");
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
