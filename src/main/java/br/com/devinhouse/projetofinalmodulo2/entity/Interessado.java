package br.com.devinhouse.projetofinalmodulo2.entity;

import javax.persistence.*;

@Entity
public class Interessado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 250)
    private String nmInteressado;

    @Column(nullable = false, length = 250, unique = true)
    private String nuIdentificacao;

    @Column(nullable = false)
    private String dtNascimento;

    @Column(nullable = false)
    private char flAtivo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNmInteressado() {
        return nmInteressado;
    }

    public void setNmInteressado(String nmInteressado) {
        this.nmInteressado = nmInteressado;
    }

    public String getNuIdentificacao() {
        return nuIdentificacao;
    }

    public void setNuIdentificacao(String nuIdentificacao) {
        this.nuIdentificacao = nuIdentificacao;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public char getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(char flAtivo) {
        this.flAtivo = flAtivo;
    }
}
