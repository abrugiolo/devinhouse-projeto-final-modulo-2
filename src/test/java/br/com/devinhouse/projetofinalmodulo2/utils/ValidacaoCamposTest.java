package br.com.devinhouse.projetofinalmodulo2.utils;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ValidacaoCamposTest {

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private InteressadoRepository interessadoRepository;

    @InjectMocks
    private ValidacaoCampos validacaoCampos;

    @Test
    public void deveRetornarFalseAoValidarDataCorreta() {

        assertTrue(validacaoCampos.validarData("2021-02-28"));
    }

    @Test
    public void deveRetornarFalseAoValidarDataIncorreta() {

        assertFalse(validacaoCampos.validarData("2021-02-31"));
    }

    @Test
    public void deveRetornarTrueAoValidarFlAtivoValido() {

        assertTrue(validacaoCampos.validarFlAtivo('s'));
    }

    @Test
    public void deveRetornarFalseAoValidarFlAtivoInvalido() {

        assertFalse(validacaoCampos.validarFlAtivo('y'));
    }

    @Test
    public void deveRetornarFalseAoValidarAssuntoCompleto() {
        AssuntoDtoInput assuntoDto = new AssuntoDtoInput();
        assuntoDto.setDescricao("descricao");
        assuntoDto.setDtCadastro("2020-01-01");
        assuntoDto.setFlAtivo('s');

        assertTrue(validacaoCampos.validarCamposPreenchidos(assuntoDto));
    }

    @Test
    public void deveRetornarFalseAoValidarAssuntoIncompleto() {

        assertFalse(validacaoCampos.validarCamposPreenchidos(new AssuntoDtoInput()));
    }

    @Test
    public void deveRetornarTrueAoValidarInteressadoCompleto() {
        InteressadoDtoInput interessadoDto = new InteressadoDtoInput();
        interessadoDto.setNmInteressado("fulano");
        interessadoDto.setNuIdentificacao("12345654321");
        interessadoDto.setDtNascimento("1950-01-01");
        interessadoDto.setFlAtivo('s');

        assertTrue(validacaoCampos.validarCamposPreenchidos(interessadoDto));
    }

    @Test
    public void deveRetornarFalseAoValidarInteressadoIncompleto() {

        assertFalse(validacaoCampos.validarCamposPreenchidos(new InteressadoDtoInput()));
    }

    @Test
    public void deveRetornarTrueAoValidarProcessoCompleto() {
        ProcessoDtoInput processoDto = new ProcessoDtoInput();
        processoDto.setSgOrgaoSetor("SOFT");
        processoDto.setNuProcesso(1);
        processoDto.setNuAno("2021");
        processoDto.setChaveProcesso("SOFT 1/2021");
        processoDto.setDescricao("descricao");
        processoDto.setCdAssunto(new Assunto());
        processoDto.setCdInteressado(new Interessado());

        assertTrue(validacaoCampos.validarCamposPreenchidos(processoDto));
    }

    @Test
    public void deveRetornarFalseAoValidarProcessoIncompleto() {

        assertFalse(validacaoCampos.validarCamposPreenchidos(new ProcessoDtoInput()));
    }
}
