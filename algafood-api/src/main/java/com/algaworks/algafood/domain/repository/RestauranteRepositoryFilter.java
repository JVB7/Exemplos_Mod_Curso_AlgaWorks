package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;


public interface RestauranteRepositoryFilter {

	List<Restaurante> buscaFiltrada(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}