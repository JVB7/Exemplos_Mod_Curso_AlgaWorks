package com.algaworks.algafood.jpa;

/*
import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
*/
public class ConsultaCozinhaMain {

}







/*
public static void main(String[] args) {
	
	//Essa interface gerencia o contexto da nossa aplicação
	ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);
	
	// SpringApplicationBuilder(AlgafoodApiApplication.class) inicializar a aplicação pegando as configurações base da classe AlgafoodApiApplication.class
	// .web(WebApplicationType.NONE) informa que não é uma aplicação do tipo web
	// applicationContext usamos para pegar um Bean da classe CadastrarCozinha já que o mesmo contém todas as instancias da inicilização
	CadastrarCozinha cadastroCozinha = applicationContext.getBean(CadastrarCozinha.class);
	
	List<Cozinha> cozinhas = cadastroCozinha.listar();
	
	for (Cozinha cozinha : cozinhas) {
		System.out.println(cozinha.getNome());
	}
	
}
*/