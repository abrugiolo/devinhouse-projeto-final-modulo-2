package br.com.devinhouse.projetofinalmodulo2.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    
    @Column(nullable = false, length = 4)
    private String sgOrgaoSetor;


    @Column(nullable = false)
    private Integer nuProcesso;

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
}
