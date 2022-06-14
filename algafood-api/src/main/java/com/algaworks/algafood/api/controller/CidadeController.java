package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
		return cidadeRepository.findAll();
	}
	
	@GetMapping("/{cidadeid}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeid) {
		
		Optional<Cidade> cidade = cidadeRepository.findById(cidadeid);
		
		if(cidade.get() != null) {
			return ResponseEntity.ok(cidade.get());
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
			Optional<Cidade> cidadePersistida = cidadeRepository.findById(cidadeid);
			BeanUtils.copyProperties(cidadeAtualizada, cidadePersistida.get(), "id");
			Cidade cidadeSalva = cadastroCidade.adicionar(cidadePersistida.get());
			return ResponseEntity.ok(cidadeSalva);
			
		}/*catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}*/catch(EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}catch(NoSuchElementException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@DeleteMapping("/{cidadeid}")
	public ResponseEntity<?> excluir(@PathVariable Long cidadeid) {
		try {
			cadastroCidade.excluir(cidadeid);
			return ResponseEntity.noContent().build();
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	
	
	
}
