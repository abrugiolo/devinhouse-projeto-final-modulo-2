package br.com.devinhouse.projetofinalmodulo2.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;

@DataJpaTest
class InteressadoRepositoryTest {
	@Autowired
	private InteressadoRepository repository;
	
	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}
	
	@Test
	void testFindByNuIdentificacao() {
		String nuIdentificacao = "12345678901";
		Optional<Interessado> interessado = repository.findByNuIdentificacao(nuIdentificacao);
		
		assertEquals(nuIdentificacao, interessado.get().getNuIdentificacao());
	}
	
	@Test
	void testExistsByNuIdentificacao() {
		String nuIdentificacao = "12345678901";
		boolean atual = repository.existsByNuIdentificacao(nuIdentificacao);
		assertTrue(atual);
	}
}