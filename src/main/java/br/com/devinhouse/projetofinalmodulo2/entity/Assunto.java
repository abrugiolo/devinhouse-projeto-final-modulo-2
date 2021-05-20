package br.com.devinhouse.projetofinalmodulo2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Assunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String dtCadastro; // String => LocalDate

    @Column(nullable = false)
    private Character flAtivo;
}
