package com.algaworks.algafood.api.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha; 
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<Cozinha> listar(){
		return cozinhaRepository.todas(); 
	}
	
	@GetMapping("/{cozinhaid}")
	public ResponseEntity<Cozinha> buscar(@PathVariable("cozinhaid") Long id) {
		
		Cozinha cozinha = cozinhaRepository.porId(id);
		
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinha.salvar(cozinha);
	}
	
	@PutMapping("/{cozinhaid}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable("cozinhaid") Long id,@RequestBody Cozinha cozinha){
		Cozinha cozinhaJaPesistida = cozinhaRepository.porId(id); 
		
		if(cozinhaJaPesistida != null) {
//			cozinhaJaPesistida.setNome(cozinha.getNome());
			BeanUtils.copyProperties(cozinha, cozinhaJaPesistida, "id");
			
			cozinhaJaPesistida = cozinhaRepository.adicionar(cozinhaJaPesistida);// faz a atualização no banco
			
			return ResponseEntity.ok(cozinhaJaPesistida);
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
