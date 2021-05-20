package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InteressadoDto {

    private Integer id;
    private String nmInteressado;
    private String nuIdentificacao;
    private String dtNascimento;
    private Character flAtivo;
}
