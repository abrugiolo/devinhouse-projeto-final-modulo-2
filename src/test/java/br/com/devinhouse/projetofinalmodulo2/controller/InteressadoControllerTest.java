package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.services.InteressadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InteressadoControllerTest {

    private static final String INTERESSADO_URL_PATH = "/interessado/v1";

    private MockMvc mockMvc;

    @Mock
    private InteressadoService interessadoService;

    @InjectMocks
    private InteressadoController interessadoController;

    private JacksonTester<Object> json;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(interessadoController).build();
    }

    @Test
    public void deveRetornarListaVaziaAoBuscarTodosOsInteressados() throws Exception {

        given(interessadoService.buscarTodosOsInteressados())
            .willReturn(new ResponseEntity(Collections.emptyList(), OK));

        mockMvc
            .perform(get(INTERESSADO_URL_PATH))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));
    }

    @Test
    public void deveRetornarInteressadoAoBuscarIdValido() throws Exception {

        InteressadoDtoOutput interessadoDto = new InteressadoDtoOutput();
        interessadoDto.setId(1);
        interessadoDto.setNmInteressado("fulano");
        interessadoDto.setNuIdentificacao("12345654321");
        interessadoDto.setDtNascimento("1590-01-01");
        interessadoDto.setFlAtivo('s');

        given(interessadoService.buscarInteressadoPeloId(1))
            .willReturn(new ResponseEntity(interessadoDto, OK));

        mockMvc
            .perform(get(INTERESSADO_URL_PATH + "/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json.write(interessadoDto).getJson()));
    }

    @Test
    public void deveRetornarInteressadoAoBuscarNuIdentificacaoValido() throws Exception {

        InteressadoDtoOutput interessadoDto = new InteressadoDtoOutput();
        interessadoDto.setId(1);
        interessadoDto.setNmInteressado("fulano");
        interessadoDto.setNuIdentificacao("12345654321");
        interessadoDto.setDtNascimento("1590-01-01");
        interessadoDto.setFlAtivo('s');

        given(interessadoService.buscarInteressadoPeloNumeroDeIdentificacao("12345654321"))
            .willReturn(new ResponseEntity(interessadoDto, OK));

        mockMvc
        .perform(get(INTERESSADO_URL_PATH + "?nu_identificacao=12345654321"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json.write(interessadoDto).getJson()));
    }

    @Test
    public void deveRetornarStatusCreatedAoCadastrarInteressadoValido() throws Exception {

        given(interessadoService.cadastrarInteressado(any(InteressadoDtoInput.class)))
            .willReturn(new ResponseEntity<>(CREATED));

        mockMvc
            .perform(post(INTERESSADO_URL_PATH)
                .contentType(APPLICATION_JSON)
                .content(json.write(new InteressadoDtoInput()).getJson()))
            .andExpect(status().isCreated());
    }

}
