package com.algaworks.algafood.jpa;
/*
import java.util.List;
import javax.persistence.TypedQuery;
*/

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;


@Component
public class CadastrarCozinha {
	
	@PersistenceContext
	private EntityManager manager;
	

	@Transactional // executa o metodo dentro de uma transação
	public Cozinha adicionar(Cozinha cozinha) {
		
		return manager.merge(cozinha); // vai fundir, colocar minha entidade dentro do contexto de persistencia
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