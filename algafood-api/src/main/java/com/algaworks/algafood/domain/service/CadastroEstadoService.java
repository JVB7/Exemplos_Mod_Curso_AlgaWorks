package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	
	public Estado salvar(Estado estado) {
		
		return estadoRepository.save(estado);
		
	}
	
	public void excluir(Long id) {
		
		try {
			estadoRepository.deleteById(id);			
		}catch(EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String
					.format("Estado id: %d não foi encontrado", id));
		}catch(DataIntegrityViolationException e) {
				throw new EntidadeEmUsoException(String.
						format("Estado com id: %d não pode ser removido, pois está sendo usado", id));
			
		}

		
	}
	
	

}
