package br.com.devinhouse.projetofinalmodulo2.services;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoOutput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.utils.ConversorLocalDateParaString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*; // is, not, empty
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AssuntoServiceTest {

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssuntoService assuntoService;

    @Test
    public void quandoBuscarTodos_DeveRetornarOkELista() {
        // given
        Assunto assunto = new Assunto(1, "descricao", LocalDate.now(), 's');
        AssuntoDtoOutput assuntoDtoOutput = new AssuntoDtoOutput(assunto.getId(), assunto.getDescricao(), assunto.getDtCadastro().toString(), assunto.getFlAtivo());

        //when
        when(modelMapper.map(assunto, AssuntoDtoOutput.class))
            .thenReturn(assuntoDtoOutput);

        when(assuntoRepository.findAll())
            .thenReturn(Collections.singletonList(assunto));

        // then
        ResponseEntity responseEntity = assuntoService.buscarTodosOsAssuntos();

        List<AssuntoDtoOutput> lista = (List<AssuntoDtoOutput>) responseEntity.getBody();

        assertEquals(OK, responseEntity.getStatusCode());
        assertThat(lista, is(not(empty())));
        assertThat(lista.get(0), is(equalTo(assuntoDtoOutput)));
    }

    @Test
    public void quandoBuscarTodos_DeveRetornarOkEMensagem() {
        // when
        when(assuntoRepository.findAll())
            .thenReturn(Collections.EMPTY_LIST);

        // then
        ResponseEntity responseEntity = assuntoService.buscarTodosOsAssuntos();

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals("Não existem assuntos cadastrados.", responseEntity.getBody().toString());
    }
    
}
