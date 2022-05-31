package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.infrastructure.repository.CozinhaRepositoryImp;

public class RemoverCozinhaMain {
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);

		CozinhaRepositoryImp cozinhas = applicationContext.getBean(CozinhaRepositoryImp.class);
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(1L);
		cozinhas.remover(cozinha.getId());
		
	}

	
}
