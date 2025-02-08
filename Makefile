# Execute na raíz do projeto o comando: make test
test: unit-test start-app integration-test system-test performance-test

build:
	@echo.
	@echo.
	@echo ::::::: Compilando a aplicacao :::::::::::::::::::::::::::::::::::::::::::::::::
	mvn compile

unit-test:
	@echo.
	@echo.
	@echo ::::::: Executando testes unitarios ::::::::::::::::::::::::::::::::::::::::::::
	mvn test

integration-test:
	@echo.
	@echo.
	@echo ::::::: Executando testes integrados ::::::::::::::::::::::::::::::::::::::::::::
	mvn test -P integration-test

system-test:
	@echo.
	@echo.
	@echo ::::::: Executando testes de sistema ::::::::::::::::::::::::::::::::::::::::::::
	mvn test -P system-test

performance-test:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test

start-app:
	@echo.
	@echo.
	@echo ::::::: Iniciando a aplicacao ::::::::::::::::::::::::::::::::::::::::::::
	mvn spring-boot:start
