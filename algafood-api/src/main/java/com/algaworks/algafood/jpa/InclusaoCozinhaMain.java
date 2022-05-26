package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

public class InclusaoCozinhaMain {
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);

		CozinhaRepository cozinhas = applicationContext.getBean(CozinhaRepository.class);
		
		Cozinha nova_cozinha1 = new Cozinha();
		nova_cozinha1.setNome("Brasileira");
		
		Cozinha nova_cozinha2 = new Cozinha();
		nova_cozinha2.setNome("Italiana");
		
		nova_cozinha1 = cozinhas.adicionar(nova_cozinha1);
		nova_cozinha2 = cozinhas.adicionar(nova_cozinha2);
		
		System.out.printf("%d - %s\n", nova_cozinha1.getId(), nova_cozinha1.getNome());
		System.out.printf("%d - %s\n", nova_cozinha2.getId(), nova_cozinha2.getNome());
	}

	
}
