package br.com.devinhouse.projetofinalmodulo2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Interessado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 250)
    private String nmInteressado;

    @Column(nullable = false, length = 250, unique = true)
    private String nuIdentificacao;

    @Column(nullable = false)
    private String dtNascimento; // String => LocalDate

    @Column(nullable = false)
    private Character flAtivo;
}
