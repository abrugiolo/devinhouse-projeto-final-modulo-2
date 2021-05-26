package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.services.AssuntoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssuntoControllerTest {

    private static final String ASSUNTO_URL_PATH = "/assunto/v1";

    private MockMvc mockMvc;

    @Mock
    private AssuntoService assuntoService;

    @InjectMocks
    private AssuntoController assuntoController;

    private JacksonTester<Object> json;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(assuntoController).build();
    }

    @Test
    public void deveRetornarListaVaziaAoBuscarTodosOsAssuntos() throws Exception {

        given(assuntoService.buscarTodosOsAssuntos())
            .willReturn(new ResponseEntity(Collections.emptyList(), OK));

        mockMvc
            .perform(get(ASSUNTO_URL_PATH))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));
    }

    @Test
    public void deveRetornarListaDeAssuntosAoBuscarTodosOsAssuntos() throws Exception {

        List<AssuntoDtoOutput> listaAssuntos = Arrays.asList(new AssuntoDtoOutput(), new AssuntoDtoOutput(), new AssuntoDtoOutput());
        listaAssuntos.get(0).setId(1);
        listaAssuntos.get(1).setId(2);
        listaAssuntos.get(2).setId(3);

        given(assuntoService.buscarTodosOsAssuntos())
            .willReturn(new ResponseEntity(listaAssuntos, OK));

        mockMvc
            .perform(get(ASSUNTO_URL_PATH))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3)));
    }

    @Test
    public void deveRetornarAssuntoAoBuscarIdValido() throws Exception {

        AssuntoDtoOutput assuntoDto = new AssuntoDtoOutput(1, "descricao", "2021-02-28", 's');

        given(assuntoService.buscarAssuntoPeloId(anyInt()))
            .willReturn(new ResponseEntity(assuntoDto, OK));

        mockMvc
            .perform(get(ASSUNTO_URL_PATH + "/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json.write(assuntoDto).getJson()));
    }

    @Test
    public void deveRetornarStatusNotFoundAoBuscarIdInvalido() throws Exception {

        given(assuntoService.buscarAssuntoPeloId(100))
            .willReturn(new ResponseEntity<>(NOT_FOUND));

        mockMvc
            .perform(get(ASSUNTO_URL_PATH + "/100"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarStatusCreatedAoCadastrarAssuntoValido() throws Exception {

        given(assuntoService.cadastrarAssunto(ArgumentMatchers.any(AssuntoDtoInput.class)))
            .willReturn(new ResponseEntity<>(CREATED));

        mockMvc.perform(post(ASSUNTO_URL_PATH)
                .contentType(APPLICATION_JSON)
                .content(json.write(new AssuntoDtoInput()).getJson()))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarStatusBadRequestAoCadastrarAssuntoInvalido() throws Exception {

        given(assuntoService.cadastrarAssunto(ArgumentMatchers.any(AssuntoDtoInput.class)))
            .willReturn(new ResponseEntity<>(BAD_REQUEST));

        mockMvc.perform(post(ASSUNTO_URL_PATH)
                .contentType(APPLICATION_JSON)
                .content(json.write(new AssuntoDtoInput()).getJson()))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

}
