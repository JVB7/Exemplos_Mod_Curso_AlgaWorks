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


#Desafio: Lombok e repositório de restaurantes

	1º Alterar a classe Restaurante (usar as notações lombok)
	2º Criar a interface RestauranteRepository e seus metodos
	3º Criar a classe RestauranteRepositotyImp                // fazer as implmentações dos metodos da INTERFACE RestauranteRepository
	4º Alterar o arquivo import.sql e adicionaremos comandos insert para popularmos a tabela de restaurantes ao iniciar a aplicação

#Mapeando relacionamento com @ManyToOne

	1º Criar o relacionamento entre a classe Restaurante e classe Cozinha, atravez do atributo cozinha e a notação @ManyToOne
		@ManyToOne
		private Cozinha cozinha

	2º Usando propriedades vamos setar o gerador da foreign key (DDL do banco)
		
		=> Ctrl+Shift+t para pesquisar o dialect, ao abrir, pegará o nome do pacote 'org.hibernate.dialect' e nome da classe 'MySQL8Dialect'

		:: spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect

	3º Vamos referncia uma cozinha para um restaurante passando seu codigo(key)

		=> ex: Cozinha 'Tailandesa' : insert into cozinha (id, nome) values (2,'Tailandesa');                                  primary key (2)
		       Restaurante 'Ecologia' : insert into restaurante (nome, taxa_frete, cozinha_id) values ('Ecologia', 7, 2);      foreing key (2)

# A anotação @JoinColumn

	1º Referenciando uma coluna do banco de dados, ao invés de suar @Column será usado @JoinColumn
		ex: 
		    @ManyToOne
		    @JoinColumn(name="cozinha_id")
		    private  Cozinha cozinha;


# Propriedade nullable de @Column e @JoinColumn
	
	=> Se olharmos no banco, perceberemos que as colunas aceitam valores null, e o correto seria notnull

	1º Alterando o valor padrão nullable(true) para false
		ex:
		   @Column(name="taxa_frete", nullable=false)  // ISSO NÃO VAI FAZER A COLUNA, NÃO ACEITAR NULO, COMO TBM INFLUENCIARA NO TIPO JOIN (fetching)
        	   private BigDecimal taxaFrete;

# Desafio: mapeando entidades

	1º Criando as entidades:

		=> Estado, Cidade, FormaPegamento, Permissao

		OBS: Adicionaremos as anotações do JPA e do Lombok: @Data, @EqualsAndHashCode(onlyExplicitlyIncluded = true), @Entity

	2º Criando as interfaces dos repositórios
	
		=> EstadoRepository, CidadeRepository, FormaPagamentoRepository, PermissaoRepository
		
		=> Metodos:
			   List<Classe> listar();
        		   Classe buscar(Long id);
		           Classe salvar(Classe objeto);
		           void remover(Classe objeto);

	3º Implementando os repositórios

		=> EstadoRepositoryImp, CidadeRepositoryImp, FormaPagamentoRepositoryImp, PermissaoRepositoryImp

	4º Popular nossas tabelas

	5º Criando as classes para testarmos

		=> ConsultaCidadeMain, ConsultarFormaPagamentoMain, ConsultarPermissaoMain


*********************************************************** ( MODULO 4 ) **************************************************************************

(USAR API) -- REQUISIÇÃO -- > PROVEDOR DA API   // A RESPOSTA PODE SER NO FORMATO JSON OU XML
      

#4.1 O que é REST? (BASEADO NA TESE, Projeto de arquitetura de Software baseado em rede)

	=> São prinncípios de arquitetura, para os desenvolvedores criarem suas APIs

	=> REpresentational State Transfer (não é uma tecnoligia), é uma especificação ou modelo arquitetural
	=> Especifica a forma de comunicação de componentes na web, independente da linguagem de programação usada
	=> São regras CONSTRAINTS
	
	=> CLIENTE --> requisição --> Provedor
	=> CLIENTE <-- resposta <-- Rest API

	=> Vantagens de Rest:
		-> Separaçao entre cliente e servidor
		-> Escalabilidade
		-> Indepêndencia de linguagem


#4.2. Conhecendo as constraints do REST (Princípios de aplicações distribuídas)

	=> CONSTRAINTS:
		1º Cliente - Servido: haverá a comunicação/cosumo da API. Deve existe a evolução das aplicações, independente da outra.
			     Um cliente ou servidor pode ser substituido desde que as interfaces permaneção intactas

		2º Stateliss: Sem estado, ou seja a aplicação não deve ter estados. (inspirado do protocolo http)
			      Logo uma requisição feita deve conter tudo que é nescessário para que seja devidamente processada
			      O servidor não sabe quem é o cliente

				(** JWT- JSON Web Token **) // Gera um token e armazena no cliente, tirando a responsabilidade do servidor
				- padrão da Internet para a criação de dados com assinatura opcional e/ou criptografia 
				cujo payload(Carga útil) contém o JSON que afirma algum número de declarações

		3º Cache: A API pode fazer chache das respostas das requisições,
			  Logo na proxima requisição antes da requisição chegar no provedor/servidor o cache entra em ação 
			  Proxy: servidor intermediario para fazer chache de determinadas requisições
			  Diminui o numero de hits na rede
	
			  OBS: não em toda aplicação, quando nescessário

		4º Interface Uniforme: Um conjuntos de operações bem definidos do sistema

		
		=> Em resumo, as API deve ser desenvolvidas usando o verbo http, de que as interfaces funcione como um contrato,
		   onde o servidor e o cliente posso se comunicar de uma forma mais previsivel,
		   adicionando links entre os recursos e devolvendo uma resposta com informações padronizadas.

		5º Sistema em Camadas: Significa entre o Cliente e Servidor, pode existir outros servidores(serv_segurança, serv_chace_,...) no meio do caminho 
				       Não pode afetar a requisição nem a resposta
	
		6º Código sob demanda: Opcional, pouco usa nas APIs,
				       O servidor pode mandar algum código para ser executado no cliente




#4.3. Diferença entre REST e RESTful

	=> REST: é o estilo arquiterural que possui as constraints, a especifição.
	=> RESTful: é uma API construida em conformidade com as constraints, segue todas as constrainst religiosamente.

#4.4. Desenvolvedores de REST APIs puristas e pragmáticos

#4.5. Conhecendo o protocolo HTTP   (SEGUIR AS SEMÂNTICAS DOS VERBOS HTTP)

	=> 'REQUISIÇÃO', composição da requsição:


		     **LADO CLIENTE**				**LADO SERVIDOR**

		GET /artigo-rest HTTP/1.1    --RESPOSTA--> HTTP/1.1 200 ok  (404 ou 500)
		Accept: application/json		   Content-Type: application/json
		Host: www.alura.com 			   {
								"titulo": "O que é REST",
								"conteudo": "o rest foi criado..."
							   }

	
		[MÉTODO] [URI] HTTP/[VERSÃO]               Post /produto HTTP/1.1 
		[CABEÇALHO]				   Content-Type: application/json
							   Accept: application/json

		[CORPO/PAYLOAD]				   {
								"nome": "Notebook i7",
								"preco": "2100.0"
							   }		
		
		--> [MÉTODO]: ação a ser realizada
				GET: deve ser devolvido uma resposta de uma requisição
				POST: submeti dados para o servidor	// adicionar dados no banco
				PUT: atulização
				DELETE: deletar	

		--> [URI]: é  um caminha que identifica oq queremos dentro do servidor http

		--> [CABEÇALHO]: informação/valores chaves sobre a requisição, que pode ser usado pelo servidor
				
				Content-Type: application/json: os dados estão sendo enviados no formato JSON
				Accept: application/json: A resposta deve ser no formato JSON


	=> 'RESPOSTA'

		HTTP/[VERSÃO] [STATUS]	                   HTTP/1.1  201 Created
		[CABEÇALHO]				   Content-Type: application/json

		[CORPO]					   {
								"codigo": 331,
								"nome": "Notebook i7",
								"preco": "2100.0"
							   }

#4.6. Usando o protocolo HTTP

	=> Usando uma API do github por meio de requisição http, endPoint-termino de ponto

#4.7. Instalando e testando o Postman

	=> Será usado para testar a Rest API que será desenvolvida, organiza as nossa requsições

	=> Não preciará todas vez informar os verbos http
	
	=> Usado para fazer as requisições na Rest API (umas das funcionalidades é ser um client de um rest api)

#4.8. Entendendo o que são Recursos REST

	=> É qualquer coisa posta na web (documento, page, video), algo de importancia que pode ser referenciado no software
	=> Singleton Resource - unico recurso (instancia de um objeto de um determinado tipo)
	=> Collection Resource - Coleção de recursos (Uma coleção contém zero ou muitos recursos do mesmo tipo)


#4.9. Identificando recursos REST
	
	=> URI - Uniform Resource Identifier: 
		-> Rest usa URI para identificar um recurso na web
		   ex URI: /listarProdutos -- correto (substantivo) --> /produtos

	=> URI vs URL:
		-> URL: é um tipo de URI, porém além de identificar, tambem localiza o recurso onde está o mesmo, qual o recurso para chegar até ele 


4.10. Modelando e requisitando um Collection Resource com GET
	
	** Criar nosso primeiro serviço Rest **

	1º Criar o pacote .api.controller e a classe CozinhaController 

		=> Séra responsavel por receber requisições 

		=> CozinhaController será um componente do Spring, logo terá a notação: 
			::@Controller
		=> Precisamos mapear esse controlador, pois as requisições precisam o encontrar:
			::@RequestMapping("/cozinhas")

	2º Ao encontrar o CozinhaController, precisanmos seta um metodos para ser executado
		
		=> Mapear um metodo:
			::@GetMapping    // verbo Get do http ou seja, quando fizer uma requisição 'GET /cozinhas HTTP/1.1' ira cair nesse metodo, e a resposta lista 					de cozinhas

	3º Para evitar um erro na resposta, precisamos colocar a notação:

		::@ResponseBody


	4º Existe uma notação que é composta pela notação @Controller e @Response, ele é:
		
		::@RestController

	5º Criar um requisição no Postman:

		=> Usamos o ver Get:localhost:8080/cozinhas
		=> A resposta é uma arquivo Json, contendo os objetos cozinhas

#4.11. Desafio: collection resource de estados

	=> Será criado um serviço Rest para a classe Estado


#4.12. Representações de recursos e content negotiation

	=> Representações de recursos: JSON, XML, PNG

	=> Content negotiation: é o formato do conteudo negociado para ser retornado

	1º Qual o melhor formato para representar um recurso na API ?
		R: O mais usado é o Json


#4.13. Implementando content negotiation para retornar JSON ou XML

	=> Especificar o midia type: no cabeçalho (Headers) de forma explicita
		     key:	 value:
		:: Accept:   application/Json

	=> Para funcionar o formato XML é nescessário adicionar a dependencia no arquivo pom.xml

<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-xml</artifactId>
</dependency>

	=> Espeficicando para o metododo o tipo de retorno da requisição:
		:: @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)  // logo nosso endpoint só aceita o formato xml
		:: @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})  // logo nosso endpoint só aceita o formato xml

		OU (2ºop)
		
		::@RequestMapping(value="/cozinha", produces = MediaType.APPLICATION_JSON_VALUE)

#4.14. Consultando Singleton Resource com GET e @PathVariable

	1º Criar um metodo para buscar o objeto:
		:: public Cozinha buscar(){...}

	2º Mapear a rota para esse metodo criado:

		::@GetMapping("/{cozinhaid}")          // criamos uma continuação que vai concatenar com a rota principal @RequestMapping("/cozinhas")
		  public Cozinha buscar(Long id) {...}

	3º Precisamosfazer baint, referenciar a variavel cozinhaid com id

		::@GetMapping("/{cozinhaid}")
		  public Cozinha buscar(@PathVariable("cozinhaid") Long id) {...}


#4.15. Customizando as representações XML e JSON com @JsonIgnore, @JsonProperty e @JsonRootName

	=> Setando um nome para propriedade da classe, para o mesmo ser usado durante apresentação/resposta da requisição
		
		@JsonProperty("titulo")
		private String nome; 

		@JsonIgnore // vai ignora a propriedade
		@JsonRootName("cozinhas") // porId

		






















