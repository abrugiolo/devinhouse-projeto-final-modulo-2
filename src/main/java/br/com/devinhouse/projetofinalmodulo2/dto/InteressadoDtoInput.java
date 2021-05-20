package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class InteressadoDtoInput {
    private String nmInteressado;
    private String nuIdentificacao;
    private String dtNascimento;
    private Character flAtivo;
}
