package br.com.devinhouse.projetofinalmodulo2.dto;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessoDtoOutput {
	
	private Integer id;
	private String sgOrgaoSetor;
	private Integer nuProcesso;
	private String nuAno;
	private String chaveProcesso;
	private String descricao;
	private Assunto cdAssunto;
	private Interessado cdInteressado;
}
