package br.com.devinhouse.projetofinalmodulo2.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
}
