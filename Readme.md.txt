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


















		   



















