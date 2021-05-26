package br.com.devinhouse.projetofinalmodulo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;

import java.util.Optional;

@Repository
public interface InteressadoRepository extends JpaRepository<Interessado, Integer>{

    Optional<Interessado> findByNuIdentificacao(String nuIdentificacao);

    boolean existsByNuIdentificacao(String nuIdentificacao);

}
