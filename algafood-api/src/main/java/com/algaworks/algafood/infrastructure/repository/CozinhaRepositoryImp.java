package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImp implements CozinhaRepository {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Cozinha> todas() {
		return manager.createQuery("from Cozinha", Cozinha.class).getResultList();
	}

	@Transactional // executa o metodo dentro de uma transação
	@Override
	public Cozinha adicionar(Cozinha cozinha) {
		
		return manager.merge(cozinha); // vai fundir, colocar minha entidade dentro do contexto de persistencia
	}
	
	@Override
	public Cozinha porId(Long id){
		return manager.find(Cozinha.class, id);
	}
	
	@Transactional
	@Override
	public void remover(Long id) {
		Cozinha cozinha = porId(id);
		
		if(cozinha == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		manager.remove(cozinha);
		
	}
}

/*
@PersistenceContext
private EntityManager maneger;

public List<Cozinha> listar(){
	
	TypedQuery<Cozinha> query = maneger.createQuery("from Cozinhas", Cozinha.class);
	
	return query.getResultList();
}
*/
