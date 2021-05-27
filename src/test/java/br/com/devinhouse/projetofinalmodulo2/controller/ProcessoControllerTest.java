package br.com.devinhouse.projetofinalmodulo2.controller;

import static org.hamcrest.Matchers.contains;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProcessoControllerTest {

	static final String PROCESSO_URL_PATH = "/processo/v1";
	
	private MockMvc mockMvc;
	
	@Mock
	private ProcessoService service;
	
	@InjectMocks
	private ProcessoController controller;

	private JacksonTester<Object> json;

	@BeforeEach
	void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void deveRetornarListaAoBuscarTodosOsProcessos() throws Exception {

		List<ProcessoDtoOutput> listaProcessos = Arrays.asList(new ProcessoDtoOutput(), new ProcessoDtoOutput(), new ProcessoDtoOutput());
		listaProcessos.get(0).setId(1);
		listaProcessos.get(1).setId(2);
		listaProcessos.get(2).setId(3);

		given(service.buscarTodosOsProcessos())
				.willReturn(new ResponseEntity(listaProcessos, OK));

		mockMvc
				.perform(get(PROCESSO_URL_PATH))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].id", contains(1, 2, 3)));

	}

	@Test
	public void deveRetornarProcessoAoBuscarIdValido() throws Exception {

		ProcessoDtoOutput processoDto = new ProcessoDtoOutput();
		processoDto.setId(1);
		processoDto.setNuProcesso(1);
		processoDto.setChaveProcesso("SOFT 1/2021");

		given(service.buscarProcessoPeloId(1))
				.willReturn(new ResponseEntity(processoDto, OK));

		mockMvc
				.perform(get(PROCESSO_URL_PATH + "/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(json.write(processoDto).getJson()));
	}

	@Test
	public void deveRetornarProcessoAoBuscarNuProcessoValido() throws Exception {

		ProcessoDtoOutput processoDto = new ProcessoDtoOutput();
		processoDto.setId(2);
		processoDto.setNuProcesso(2);
		processoDto.setChaveProcesso("SOFT 2/2021");

		given(service.buscarProcessoPeloNumeroProcesso(2))
				.willReturn(new ResponseEntity(processoDto, OK));

		mockMvc
				.perform(get(PROCESSO_URL_PATH + "?nu-processo=2"))
						//.param("nu-processo", "2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(json.write(processoDto).getJson()));
	}

	@Test
	public void deveRetornarListaDeProcessosAoBuscarPorInteressdaoValido() throws Exception {

		ProcessoDtoOutput processoDto = new ProcessoDtoOutput();
		processoDto.setId(3);
		processoDto.setNuProcesso(3);
		processoDto.setChaveProcesso("SOFT 3/2021");
		ProcessoDtoOutput processoDto2 = new ProcessoDtoOutput();
		processoDto.setId(4);
		processoDto.setNuProcesso(4);
		processoDto.setChaveProcesso("SOFT 4/2021");
		List<ProcessoDtoOutput> listaProcessos = Arrays.asList(processoDto, processoDto2);

		given(service.buscarProcessosPorInteressado(3))
				.willReturn(new ResponseEntity(listaProcessos, OK));

		mockMvc
				.perform(get(PROCESSO_URL_PATH + "?id-interessado=3"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(json.write(listaProcessos).getJson()));
	}

	@Test
	public void deveRetornarStatusCreatedAoCadastrarProcessoValido() throws Exception {

		given(service.cadastrarProcesso(any(ProcessoDtoInput.class)))
				.willReturn(new ResponseEntity("CREATED", CREATED));

		mockMvc
				.perform(post(PROCESSO_URL_PATH)
						.contentType(APPLICATION_JSON)
						.content(json.write(new AssuntoDtoInput()).getJson()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string("CREATED"));
	}

	@Test
	public void deveRetornarOkAoAtualizarProcesso() throws Exception {

		given(service.atualizarProcesso(eq(1), any(ProcessoDtoInput.class)))
				.willReturn(new ResponseEntity("OK", OK));

		mockMvc
				.perform(put(PROCESSO_URL_PATH + "/1")
						.contentType(APPLICATION_JSON)
						.content(json.write(new ProcessoDtoInput()).getJson()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}

	@Test
	public void deveRetornarStatusOkAoDeletarProcesso() throws Exception {

		given(service.deletarProcesso(1))
				.willReturn(new ResponseEntity("OK", OK));

		mockMvc
				.perform(delete(PROCESSO_URL_PATH + "/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}

}
