package br.com.devinhouse.projetofinalmodulo2.dto;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;

public class ProcessoDto {
	
	private Integer id;
	private Integer nuProcesso;
	private String nuAno;
	private String chaveProcesso;
	private String descricao;
	private Assunto cdAssunto;
	private Interessado cdInteressado;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNuProcesso() {
		return nuProcesso;
	}

	public void setNuProcesso(Integer nuProcesso) {
		this.nuProcesso = nuProcesso;
	}

	public String getNuAno() {
		return nuAno;
	}

	public void setNuAno(String nuAno) {
		this.nuAno = nuAno;
	}

	public String getChaveProcesso() {
		return chaveProcesso;
	}

	public void setChaveProcesso(String chaveProcesso) {
		this.chaveProcesso = chaveProcesso;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Assunto getCdAssunto() {
		return cdAssunto;
	}

	public void setCdAssunto(Assunto cdAssunto) {
		this.cdAssunto = cdAssunto;
	}

	public Interessado getCdInteressado() {
		return cdInteressado;
	}

	public void setCdInteressado(Interessado cdInteressado) {
		this.cdInteressado = cdInteressado;
	}
	
	
}
