package br.com.devinhouse.projetofinalmodulo2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssuntoDtoOutput {

    private Integer id;
    private String descricao;
    private String dtCadastro;
    private Character flAtivo;

    @Override
    public String toString() {
        return "{" + "id=" + id + ", descricao='" + descricao + '\'' + ", dtCadastro='" + dtCadastro + '\'' + ", flAtivo=" + flAtivo + '}';
    }
}
