package br.com.devinhouse.projetofinalmodulo2.repository;

import br.com.devinhouse.projetofinalmodulo2.entity.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

}
