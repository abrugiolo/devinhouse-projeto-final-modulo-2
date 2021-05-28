package br.com.devinhouse.projetofinalmodulo2.dto;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessoDtoInput {
	
	private Integer nuProcesso;
	private String sgOrgaoSetor;
	private String nuAno;
	private String chaveProcesso;
	private String descricao;
	private Assunto cdAssunto;
	private Interessado cdInteressado;
}
