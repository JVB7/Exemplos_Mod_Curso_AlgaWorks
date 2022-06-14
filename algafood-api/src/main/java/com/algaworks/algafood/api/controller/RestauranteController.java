package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@GetMapping
	public List<Restaurante> listar(){
		
		return restauranteRepository.findAll();
	}
	
	@GetMapping("/{restauranteid}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteid) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteid);
		if(restaurante.get() != null) {
			return ResponseEntity.ok(restaurante.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	/*------------------------------------------------------------------------------------*/
	
	@GetMapping("/por-taxa")
	public List<Restaurante> porTaxa(BigDecimal taxaInicial, BigDecimal taxaFinal){
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}
	
	/*------------------------------------------------------------------------------------*/
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestaurante.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}	
	}
	
	@PutMapping("/{restauranteid}") // endpoint 
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteid,@RequestBody Restaurante restauranteAtualizado) {
		
		Optional<Restaurante> restaurantePesistido = restauranteRepository.findById(restauranteid);
		if(restaurantePesistido.get() != null) {
			BeanUtils.copyProperties(restauranteAtualizado, restaurantePesistido.get(),"id");
			try {
				Restaurante restauranteSalvo = cadastroRestaurante.salvar(restaurantePesistido.get());
				return ResponseEntity.ok(restauranteSalvo);
				
			}catch(EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
				
			}	 
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@DeleteMapping("/{restauranteid}")
	public ResponseEntity<?> deletar(@PathVariable Long restauranteid){
		
		try {
			cadastroRestaurante.excluir(restauranteid);
			return ResponseEntity.notFound().build();
			
		}catch(EntidadeNaoEncontradaException e){
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
//	@PatchMapping("/{restauranteid}")
//	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteid, @RequestBody Restaurante restaurante){
//		
//	}











}
