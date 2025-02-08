build:
	@echo ::::::: Compilando a aplicacao :::::::::::::::::::::::::::::::::::::::::::::::::
	mvn compile

unit-test:
	@echo ::::::: Executando testes unitarios ::::::::::::::::::::::::::::::::::::::::::::
	mvn test

integration-test:
	@echo ::::::: Executando testes integrados ::::::::::::::::::::::::::::::::::::::::::::
	mvn test -P integration-test

system-test: start-app
	@echo ::::::: Executando testes de sistema ::::::::::::::::::::::::::::::::::::::::::::
	mvn test -P system-test

test: unit-test integration-test

start-app:
	@echo ::::::: Iniciando a aplicacao ::::::::::::::::::::::::::::::::::::::::::::
	mvn spring-boot:start
