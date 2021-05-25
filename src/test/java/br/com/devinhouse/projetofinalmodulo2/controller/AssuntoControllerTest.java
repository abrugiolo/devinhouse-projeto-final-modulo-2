package br.com.devinhouse.projetofinalmodulo2.controller;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.services.AssuntoService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssuntoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssuntoService assuntoService;

    @InjectMocks
    private AssuntoController assuntoController;

    private JacksonTester<AssuntoDtoInput> json;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(assuntoController).build();
    }

    @Test
    public void GetSemParametro_RetornaOk() throws Exception {

        given(assuntoService.buscarTodosOsAssuntos())
            .willReturn(new ResponseEntity<>(OK));

        mockMvc
            .perform(get("/assunto/v1"))
            .andExpect(status().isOk());
    }

    @Test
    public void GetComIdValido_RetornaOk() throws Exception {

        given(assuntoService.buscarAssuntoPeloId(anyInt()))
            .willReturn(new ResponseEntity<>(OK));

        mockMvc
            .perform(get("/assunto/v1/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void GetComIdInvalido_RetornaNotFound() throws Exception {

        given(assuntoService.buscarAssuntoPeloId(anyInt()))
            .willReturn(new ResponseEntity<>(NOT_FOUND));

        mockMvc
            .perform(get("/assunto/v1/100")
                .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void PostComDtoValido_RetornaCreated() throws Exception {

        given(assuntoService.cadastrarAssunto(any(AssuntoDtoInput.class)))
            .willReturn(new ResponseEntity<>(CREATED));

        mockMvc.perform(post("/assunto/v1")
                .contentType(APPLICATION_JSON)
                .content(json.write(new AssuntoDtoInput()).getJson()))
            .andExpect(status().isCreated());
    }

    @Test
    public void PostComDtoInvalido_RetornaBadRequest() throws Exception {

        given(assuntoService.cadastrarAssunto(any(AssuntoDtoInput.class)))
            .willReturn(new ResponseEntity<>(BAD_REQUEST));

        mockMvc.perform(post("/assunto/v1")
                .contentType(APPLICATION_JSON)
                .content(json.write(new AssuntoDtoInput()).getJson()))
            .andExpect(status().isBadRequest());
    }

}
