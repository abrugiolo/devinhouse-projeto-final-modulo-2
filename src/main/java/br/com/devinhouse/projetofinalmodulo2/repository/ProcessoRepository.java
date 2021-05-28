package br.com.devinhouse.projetofinalmodulo2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer>{
	List<Processo> findByCdInteressado(Interessado interessado);

	Optional<Processo> findByNuProcesso(Integer nuProcesso);

	boolean existsByChaveProcesso(String chaveProcesso);
}
