package br.com.devinhouse.projetofinalmodulo2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.devinhouse.projetofinalmodulo2.repository.ProcessoRepository;
import br.com.devinhouse.projetofinalmodulo2.entity.Interessado;
import br.com.devinhouse.projetofinalmodulo2.entity.Processo;
@Service
public class ProcessoService {
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	public List<Processo>buscarProcessosPorInteressado (Interessado interessado) {
		//tratamento para interessados inativos ?
		//tratamento para interessado inválido ?
		return processoRepository.findBycdInteressado(interessado);
	}

	public List<Processo> buscarTodosOsProcessos() {

		return processoRepository.findAll();
	}

	public Processo buscarProcessoPeloId(Integer id) {

		return processoRepository.findById(id).orElse(null);
	}

	public Processo buscarProcessoPeloNumero(Integer nuProcesso) {

		return processoRepository.findByNuProcesso(nuProcesso).orElse(null);
	}

	public Processo cadastrarProcesso(Processo processo) {

		if (processoRepository.existsByChaveProcesso(processo.getChaveProcesso()))
			throw new IllegalArgumentException("Chave do Processo já existe no sistema.");

		// TODO: verificar se interessado e assunto existem e estao ativos

		return processoRepository.save(processo);
	}

}
