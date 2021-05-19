package br.com.devinhouse.projetofinalmodulo2.entity;

import javax.persistence.*;

@Entity
public class Assunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String dtCadastro; // String => LocalDate

    @Column(nullable = false)
    private char flAtivo;

    public Assunto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public char getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(char flAtivo) {
        this.flAtivo = flAtivo;
    }
}
