package br.com.devinhouse.projetofinalmodulo2.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

import org.mockito.Mock;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.devinhouse.projetofinalmodulo2.services.ProcessoService;

class ProcessoControllerTest {
	static final String PROCESSO_URL_PATH = "/processo/v1";
	
	private MockMvc mockMvc;
	
	@Mock
	private ProcessoService service;
	
	@InjectMocks
	private ProcessoController controller;
	
	@BeforeEach
	void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	//pesquisar se deve retornar o processo tbm ou apenas o status
	/*
	 * @Test void deveRetornarProcessosInformandoInteressado() throws Exception{
	 * //given given(service.buscarProcessosPorInteressado(anyInt()))
	 * .willReturn(new ResponseEntity<>(OK));
	 * 
	 * //when
	 * 
	 * //then mockMvc .perform(get(PROCESSO_URL_PATH + "?id-interessado=" ))
	 * .andExpect(status().isOk()); }
	 */
	
	/*
	 * @Test void deveRetornarProcessoInformandoIdProcesso() {
	 * 
	 * }
	 * 
	 * @Test void deveRetornarProcessoInformandoNumeroProcesso() {
	 * 
	 * }
	 * 
	 * @Test void deveRetornarListaDeTodosOsProcessos() {
	 * 
	 * }
	 * 
	 * @Test void testCadastrarProcesso() {
	 * 
	 * }
	 * 
	 * @Test void testAtualizarCadastro() {
	 * 
	 * }
	 * 
	 * @Test void testDeletarProcesso() {
	 * 
	 * }
	 */

}
