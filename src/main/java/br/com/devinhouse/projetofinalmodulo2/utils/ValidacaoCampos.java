package br.com.devinhouse.projetofinalmodulo2.utils;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidacaoCampos {

    public static boolean validarData(String s) {
        try {
            LocalDate.parse(s);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validarFlAtivo(Character c) {
        return Character.toLowerCase(c) == 's' || Character.toLowerCase(c) == 'n';
    }

    public static boolean validarCamposPreenchidos(Object object) {
        if (object.getClass() == AssuntoDtoInput.class) {
            AssuntoDtoInput assunto = (AssuntoDtoInput) object;
            return assunto.getDescricao() != null && assunto.getDtCadastro() != null && assunto.getFlAtivo() != null;
        }

        if (object.getClass() == InteressadoDtoInput.class) {
            InteressadoDtoInput interessado = (InteressadoDtoInput) object;
            return interessado.getNmInteressado() != null && interessado.getNuIdentificacao() != null && interessado.getDtNascimento() != null && interessado.getFlAtivo() != null;
        }

        if (object.getClass() == ProcessoDtoInput.class) {
            ProcessoDtoInput processo = (ProcessoDtoInput) object;
            return processo.getSgOrgaoSetor() != null && processo.getNuAno() != null && processo.getDescricao() != null && processo.getCdAssunto() != null && processo.getCdInteressado() != null;
        }

        return false;
    }
}
