# Execute na raíz do projeto o comando: make test
test: unit-test start-app integration-test system-test performance-test-all

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

performance-test-all:
	@echo.
	@echo.
	@echo ::::::: Executando todos testes de perfomance ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-restaurante
	mvn gatling:test -Pperformance-test-usuario
	mvn gatling:test -Pperformance-test-mesa
	mvn gatling:test -Pperformance-test-funcionamento
	mvn gatling:test -Pperformance-test-avaliacao
	mvn gatling:test -Pperformance-test-reserva

performance-test-restaurante:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance restaurante ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-restaurante

performance-test-usuario:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance usuario ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-usuario

performance-test-mesa:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance mesa ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-mesa

performance-test-funcionamento:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance funcionamento ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-funcionamento

performance-test-avaliacao:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance avaliacao ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-avaliacao

performance-test-reserva:
	@echo.
	@echo.
	@echo ::::::: Executando testes de perfomance reserva ::::::::::::::::::::::::::::::::::::::::::::
	mvn gatling:test -Pperformance-test-reserva

start-app:
	@echo.
	@echo.
	@echo ::::::: Iniciando a aplicacao ::::::::::::::::::::::::::::::::::::::::::::
	mvn spring-boot:start
