package com.algaworks.algafood.api.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha; 
	
//	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@GetMapping
	public List<Cozinha> listar(){
		return cozinhaRepository.findAll(); 
	}
	
	@GetMapping("/{cozinhaid}")
	public ResponseEntity<Cozinha> buscar(@PathVariable("cozinhaid") Long id) {
		System.out.println(id);
		Optional<Cozinha> cozinha = cozinhaRepository.findById(id);
		
		if(cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/*------------------------------------------------------------------------------------*/
	@GetMapping("/por-nome")
	public List<Cozinha> porNome(@RequestParam("nome") String nome){
		return cozinhaRepository.findByNome(nome);
	}
	
	/*------------------------------------------------------------------------------------*/
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinha.salvar(cozinha);
	}
	
	@PutMapping("/{cozinhaid}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable("cozinhaid") Long id,@RequestBody Cozinha cozinha){ 
		Optional<Cozinha> cozinhaOp = cozinhaRepository.findById(id);
		
		if(cozinhaOp.isPresent()) {
//			cozinhaJaPesistida.setNome(cozinha.getNome());
			BeanUtils.copyProperties(cozinha, cozinhaOp.get(), "id");
			
			Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaOp.get());// faz a atualização no banco
			
			return ResponseEntity.ok(cozinhaSalva);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{cozinhaid}")
	public ResponseEntity<Cozinha> remover(@PathVariable("cozinhaid") Long id){
		try {
			cadastroCozinha.excluir(id);
			return ResponseEntity.noContent().build();
			
		}catch(EntidadeNaoEncontradaException e){
			return ResponseEntity.noContent().build();
		}
		catch(EntidadeEmUsoException e){ // DataIntegrityViolationException Substituida por EntidadeEmUsoException
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	
	}
}
