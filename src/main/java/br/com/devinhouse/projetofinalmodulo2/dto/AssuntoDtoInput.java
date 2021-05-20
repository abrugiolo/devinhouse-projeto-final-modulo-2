package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class AssuntoDtoInput {
    private String descricao;
    private String dtCadastro;
    private Character flAtivo;
}
