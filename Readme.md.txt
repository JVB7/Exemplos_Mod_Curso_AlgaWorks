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