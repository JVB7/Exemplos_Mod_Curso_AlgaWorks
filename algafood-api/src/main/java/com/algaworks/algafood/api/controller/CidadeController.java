package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade; 
	
	
	@GetMapping
	public List<Cidade> listar(){
		return cidadeRepository.listar();
	}
	
	@GetMapping("/{cidadeid}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeid) {
		
		Cidade cidade = cidadeRepository.buscar(cidadeid);
		
		if(cidade != null) {
			return ResponseEntity.ok(cidade);
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			cidade = cadastroCidade.adicionar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/{cidadeid}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeid,@RequestBody Cidade cidadeAtualizada){
		
		try {
			Cidade cidadePersistida = cidadeRepository.buscar(cidadeid);
			BeanUtils.copyProperties(cidadeAtualizada, cidadePersistida, "id");
			cidadePersistida = cadastroCidade.adicionar(cidadePersistida);
			return ResponseEntity.ok(cidadePersistida);
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}catch(EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{estadoid}")
	public ResponseEntity<?> excluir(@PathVariable Long cidadeid) {
		try {
			cadastroCidade.excluir(cidadeid);
			return ResponseEntity.noContent().build();
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	
	
	
}
