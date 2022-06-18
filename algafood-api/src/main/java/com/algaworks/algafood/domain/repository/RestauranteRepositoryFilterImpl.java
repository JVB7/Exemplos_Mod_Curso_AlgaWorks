package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

import antlr.StringUtils;

@Repository
public class RestauranteRepositoryFilterImpl implements RestauranteRepositoryFilter {
	
	@PersistenceContext
	private EntityManager manage;

	@Override
	public List<Restaurante> buscaFiltrada(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = manage.getCriteriaBuilder();
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		
		Root<Restaurante> root=  criteria.from(Restaurante.class);
		
		var listaPredicados = new ArrayList<Predicate>();
		
		if(org.springframework.util.StringUtils.hasText(nome)) {
			listaPredicados.add(builder.like(root.get("nome"), "%"+nome+"%"));
		}
		
		if(taxaFreteInicial != null) {
			listaPredicados.add(builder.greaterThan(root.get("taxaFrete"), taxaFreteInicial));
		}
		if(taxaFreteFinal != null) {
			listaPredicados.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
//		Predicate predicadoNome = builder.like(root.get("nome"), "%"+nome+"%");
//		Predicate predicadotaxa = builder.between(root.get("taxaFrete"), taxaFreteInicial, taxaFreteFinal);
		
		criteria.where(listaPredicados.toArray(new Predicate[0]));
		
		TypedQuery<Restaurante> query = manage.createQuery(criteria);
		
		
		return query.getResultList();
		
		
		
//		String jpql = "From Restaurante where nome like :nome and taxaFrete between :taxaFreteInicial and :taxaFreteFinal";
//		
//		return manage.createQuery(jpql, Restaurante.class)
//		.setParameter("nome", "%"+nome+"%")
//		.setParameter("taxaFreteInicial", taxaFreteInicial)
//		.setParameter("taxaFreteFinal", taxaFreteFinal)
//		.getResultList();
		
//		StringBuilder jpql = new StringBuilder();
//		jpql.append("From Restaurante where 0 = 0 ");
//		
//		HashMap<String, Object> parametros = new HashMap<String, Object>();
//		
//		if(org.springframework.util.StringUtils.hasLength(nome)){
//			jpql.append("and nome like :nome ");
//			parametros.put("nome", "%"+nome+"%");
//		}
//		if(taxaFreteInicial != null) {
//			jpql.append("and taxaFrete >= :taxaFreteInicial ");
//			parametros.put("taxaFreteInicial", taxaFreteInicial);
//		}
//		if(taxaFreteFinal != null) {
//			jpql.append("and taxaFrete <= :taxaFreteFinal ");
//			parametros.put("taxaFreteFinal", taxaFreteFinal);
//		}
//		
//		TypedQuery<Restaurante> queryFilter = manage.createQuery(jpql.toString(), Restaurante.class);
//		
//		parametros.forEach((chave, valor) -> queryFilter.setParameter(chave, valor));
//		
//		return queryFilter.getResultList();
		
	}

}
