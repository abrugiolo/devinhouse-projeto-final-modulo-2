package br.com.devinhouse.projetofinalmodulo2.entity;

import javax.persistence.*;

@Entity
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int nuProcesso;

    @Column(nullable = false, length = 4)
    private String nuAno;

    @Column(nullable = false, length = 50, unique = true)
    private String chaveProcesso;

    @Column(nullable = false, length = 250)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_assunto")
    private Assunto cdAssunto;

    @ManyToOne
    @JoinColumn(name = "id_interessado")
    private Interessado cdInteressado;

    public Processo() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNuProcesso() {
        return nuProcesso;
    }

    public void setNuProcesso(int nuProcesso) {
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
