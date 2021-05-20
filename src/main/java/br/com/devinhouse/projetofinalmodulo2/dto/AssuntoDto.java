package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AssuntoDto {

    private Integer id;
    private String descricao;
    private String dtCadastro;
    private Character flAtivo;
}
