package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class InteressadoDtoOutput {

    private Integer id;
    private String nmInteressado;
    private String nuIdentificacao;
    private String dtNascimento;
    private Character flAtivo;
}
