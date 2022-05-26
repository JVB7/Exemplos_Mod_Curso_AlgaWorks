# Adicionando JPA e configurando o Data Source
	
	--> 1º Add JPA no projeto:
		=> Em add starters, selecionar: Spring Data JPA (pacote completo, incluindo o Hibernate)

	-->2º Conexão com Banco de Dados:
		=> spring.datasource.url= (drivee de conexão)                   //URL de conexão 
		=> (driver de conexão) 
		=> (driver de conexão) protocol//[hosts][/database][?properties]
		=> (driver de conexão) jdbc:mysql://host1:33060/sakila          // exemplo do MySQL
		=> (driver de conexão) jdbc:mysql://localhost/algafood?creatDatabaseIfNotExist=true&serverTimezone=UTC

		:: spring.datasource.url = jdbc:mysql://localhost/algafood?creatDatabaseIfNotExist=true&serverTimezone=UTC
	
	-->3º Configurando o User do banco de dados:
		:: spring.datasource.username=root
		:: spring.datasource.password=db@Inf0@17

	-->4º Falta adicionar Driver do MySQL:
		=> Em add starters, selecionar: MySQL Driver

#Mapeando entidades com JPA

	--> 1º Criar as entidades (classes) para depois fazer o mapeamento para banco de dados:

		=> No pacote : com.algaworks.algafood.domain.model

			Classe Cozinha       (Representa uma entidade @Entity) (@Table(name="nomedatabeladoBanco"))
				obs: toda entidade (classe) deve haver uma atributo identificador, como um registro no banco 'id' key
				   @Id
				:: private Long id;
				   @Column(name = "nomedoAtributo")
				:: private String nome;

			Classe Restaurante   (Representa uma entidade @Entity)

#Criando as tabelas do banco a partir das entidades

	--> 1º Configurar o JPA para gerar o comando DDL

		:: spring.jpa.generate-ddl=true            // Criar automaticamente as tabelas a partir das entidades(classes)
		:: spring.jpa.hibernate.ddl-auto=Create   // Como será feito? Drop e recriar, quando rodar a aplicação


#Mapeando o id da entidade para autoincremento

	--> 1º No atributo da entidade(Classe), adicionar:

		=> @GeneratedValue(PassaEstratejaDeGeraçãoValorAtrib)
		   @GeneratedValue(strategy = GenerationType.IDENTITY)   // o provedor de persistência irá gerar essa chave
  		   private Long id;


#Importando dados de teste com import.sql

	--> 1º Alimentado as tabelas criadas para usar em ambiente de teste
		=> criar um arquivo: import.sql, dentro dele:
			:: insert into cozinha (nome) values ('Tailandesa');
			:: insert into cozinha (nome) values ('Indiana');

#Consultando objetos do banco de dados

	--> 1º Criar uma classe para implementar essa consulta:

		=> No pacote : com.algaworks.algafood.jpa
			- classe CadastroCozinha (será um @Component)
			- metodo List<Cozinha> listar()

	--> 2º Criar uma variavel do tipo EntityManager
		=> Com ela faremos as consultas e alterações no Banco de dados (injeção @PersistenceContext)

	--> 3º Criar uma Classe para executar uma aplicação não Web (inicia  e finaliza)
		=> No pacote : com.algaworks.algafood.jpa
			- classe ConsultaCozinhaMain

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

	--> 4º Printar as consultas geradas:
		:: spring.jpa.show-sql=true



#Adicionando um objeto no banco de dados

	1º Na classe CadastroCozinha, termos:
		=> public Cozinha adicionar(Cozinha cozinha){} (vai representar uma @Transactional)
		=> Com a variavel manager e metodo merge, fazemos a junção

	2º Na classe IncluirCozinhaMain, vamos:
		=> Criar e incluir as novas cozinhas, usando:
			- applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class) // Para pegar as classes instaciadas pelo spring
			- cadastroCozinha = applicationContext.getBean(CadastrarCozinha.class);           // Instacia um objeto das instanc. da inciaização
			- coz1 = cadastroCozinha.adicionar(coz1);                                         // No final adiciona no contexto de persistencia
			- coz1 // contém agora um objeto persistido no banco com Id (autoIncrement já gerado)


#Buscando um objeto pelo id no banco de dados

	1º Na classe CadastroCozinha, termos:
		=> public Cozinha buscar(Long id){}
		=> Com a variavel manager e metodo find, fazemos a busca
	
	2º Na classe BuscarCozinhaMain, vamos:
		=> Continuar com a variavel de contexto, pois precisamos:
				- inicia e finalizar uma plaicação não web e
				- pegar dessa variavel de contextos as instancias contidadas no conteiner do spring


#Atualizando um objeto no banco de dados

	--> 1º Sem fazer alterações na Classe CadastroCozinha, criamos então uma classe AlteracaoCozinha, nela:
		=> Um objeto Cozinha, que ira receber o valor de um Id já existente e
		=> com o mesmo metodo adicionar vamos fazer a ATUALIZAÇÃO desse objeto no banco
		=> usamo apenas o metodo merge de variavel manager

	--> 1º Alteramos o metodo adicionar para salvar(), já que add e update 

#Excluindo um objeto do banco de dados

	--> 1º Em CadastrarCzinha, criamos um metodo remover() (vai representar uma @Transactional)
		=> usando a variavel manager chameros o metodo remove // removerá um objeto 
	

	--> 2º Pega a instancia que está estado TRANSIENT e passar para o estado REMOVED, 
		=> TUDO ISSO SENTO INTERMEDIADO PELO ESTADO 'MANAGED'
		
		2.1º Então vamos buscar o objeto 'buscar(cozinha)', o retorno será um objeto no estado REMOVED 

	APOIO: https://blog.algaworks.com/tutorial-jpa/

#Conhecendo o padrão Aggregate do DDD

	- DDD Domain-Driven-Design, Desing orientado por domínio
	=> Um agregado DDD é um conjunto de objetos de domínio que
	=> podem ser tratados como uma única unidade.
	=> exemplo: AGREGADO PEDIDO, é o conjunto de todas as aparte que constitue um pedido, tratado como um único agregado

	1º Um agregado terá um de seus objetos componentes ser a raiz agregada.

		- <<Interface>>
				<<Aggregate Pedido>> (raiz agregada)

	2º Quaisquer referências de fora do agregado só devem ir para a raiz agregada. (ou seja, outra <<Interface>>)

	3º Agregados são o elemento básico de transferência de armazenamento de dados 
	
		=> você solicita carregar ou salvar agregados inteiros. 
		=> As transações não devem cruzar os limites agregados.
	


	OBS: Os agregados DDD às vezes são confundidos com collection classes (lists, maps, etc)
	     Os agregados DDD são conceitos de domínio (ordem, visita clínica, lista de reprodução)
             Um agregado muitas vezes conterá coleções mutliple, juntamente com campos simples.

#Conhecendo e implementando o padrão Repository (orientado a persistencia)(CAMADAS, RESPONSABILIDADES)

	=> Padrão que adiciona mais uma camada para acesso aos dados, usa uma coleção
		-> Quem usa não precisa saber qual o mecanismo de persistencia de dados está sendo usado

	=> É criado por agregado

	1º Criar uma interface, CozinhaRepository (local onde será armezado as cozinhas)
		=> dentro do pacote: com.algaworks.algafood.domain.repository
		=> dentro de CozinhaRepository:
			-> Lista<Cozinha> listar(); -- renomear--> Lista<Cozinha> todas();
			-> Cozinha buscar(Long id); -- renomear--> Cozinha porId(Long id);
			-> Cozinha salvar(Cozinha cozinha); -- renomear--> Cozinha adicionar(Cozinha cozinha);
			-> void remover(Cozinha cozinha);

	2º Criar classe CozinhaRepositoryImp que implementa de CozinhaRepository
		=> dentro do pacote: com.algaworks.algafood.infrastructure.repository

	3º Exlcuir a classe CadastroCozinha e redefinir a referencia da mesma nas outras classes para CozinhaRepositoryImp
		


#Conhecendo e usando o Lombok

	=> Biblioteca Lombok:  

		1º Em add starters, buscar por Lombok e fazer a instalação

		2º Download no site do Lombok

		3º Apagar o metodos getter e setter da classe Cozinha e adicionar:
			=> A notação: 
				- @Getter e @Setter
				- @EqualsandHashCode

			=> @Data // contém todos os anteriores
				- @EqualsAndCode(onlyExplicitlyIncluded = true) 
				  // mesmo já contendo no @Data, quero q mesmo seja usado somente explicitamente na variavel, ex: 'Id'



































