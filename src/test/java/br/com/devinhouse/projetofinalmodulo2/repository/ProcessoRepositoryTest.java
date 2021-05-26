package br.com.devinhouse.projetofinalmodulo2.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;


@DataJpaTest
class ProcessoRepositoryTest {
	@Autowired
	private ProcessoRepository repository;

	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}
	
	@Test
	void testFindBycdInteressado() {
		Interessado interessado = new Interessado(1, "Joao", "12345678901", LocalDate.parse("2000-05-19"), 's');
		List<Processo> processoAtual = repository.findByCdInteressado(interessado);
		assertThat(processoAtual).asList().hasAtLeastOneElementOfType(Processo.class);
	}

	@Test
	void testFindByNuProcesso() {
		int nuProcesso = 1;
		Optional<Processo> processoAtual = repository.findByNuProcesso(nuProcesso);

		assertEquals(nuProcesso, processoAtual.get().getNuProcesso());
	}

	@Test
	void testExistsByChaveProcesso() {
		String chaveProcesso = "SOFT 2/2021";

		boolean esperado = repository.existsByChaveProcesso(chaveProcesso);

		assertTrue(esperado);
	}
}