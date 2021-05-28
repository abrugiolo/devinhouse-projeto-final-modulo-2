package br.com.devinhouse.projetofinalmodulo2.utils;

import br.com.devinhouse.projetofinalmodulo2.dto.AssuntoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.InteressadoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.dto.ProcessoDtoInput;
import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.repository.AssuntoRepository;
import br.com.devinhouse.projetofinalmodulo2.repository.InteressadoRepository;

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

    public static boolean validadorInteressadoInativo(InteressadoRepository interessadoRepository, Interessado interessado){

            if (interessado != null) {
            	interessado = interessadoRepository.findById(interessado.getId()).orElse(null);
        }
            if (interessado == null || interessado.getFlAtivo().equals('n')) {
            	return true;
            }
        return false;
    }

    public static boolean validadorAssuntoInativo(AssuntoRepository assuntoRepository, Assunto assunto) {
        if (assunto != null) {
            assunto = assuntoRepository.findById(assunto.getId()).orElse(null);

            if (assunto == null || assunto.getFlAtivo().equals('n')) {
                return true;
            }
        }
        return false;
    }

}