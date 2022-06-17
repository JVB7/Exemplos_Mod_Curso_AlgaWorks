package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public class RestauranteRepositoryFilterImpl implements RestauranteRepositoryFilter {
	
	@PersistenceContext
	private EntityManager manage;

	@Override
	public List<Restaurante> buscaFiltrada(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		String jpql = "From Restaurante where nome like :nome and taxaFrete between :taxaFreteInicial and :taxaFreteFinal";
		
		return manage.createQuery(jpql, Restaurante.class)
		.setParameter("nome", "%"+nome+"%")
		.setParameter("taxaFreteInicial", taxaFreteInicial)
		.setParameter("taxaFreteFinal", taxaFreteFinal)
		.getResultList();

	}

}
