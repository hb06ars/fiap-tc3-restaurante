# RESTAURANTE

### APLICAÇÃO RAILWAY
- https://fiap-tc3-restaurante-production.up.railway.app/usuario?celular=1188887777

### APLICAÇÃO AZURE
- https://fiap-tc3-restaurante-bpfhccg4d2gxdmhj.eastus-01.azurewebsites.net/restaurante/usuario?celular=1188887777


### SUBIR APLICAÇÃO LOCAL (H2)
- No arquivo src/main/resources/application.yml altere o profile para test
- Em seguida, apontar o Enviroments Variables para o arquivo: /env/variaveis-test.env
- docker build -t fiap-tc3-restaurante .
- docker run -p 8080:8080 fiap-tc3-restaurante
- Por fim, aguarde iniciar a aplicação.

## TESTES
- No arquivo Makefile temos diversos testes:
- Ele executará todos os testes necessários e também gerar os resultados utilizando o comando:
  > **make test**

### TESTES ESPECÍFICOS
- Testes unitários:
  > **mvn test**

- Testes integrados (a aplicação deve estar iniciada):
  > **mvn test -P integration-test -Dspring.profiles.active=test**

- Testes de Sistema (a aplicação deve estar iniciada):
  > **mvn test -P system-test -Dspring.profiles.active=test**

- Testes de perfomance:
  > **mvn spring-boot:start gatling:test -Pperformance-test**

- Cucumber:
  > Abra o arquivo: **<seu-diretorio>\fiap-tc3-restaurante\target\cucumber-reports\cucumber.html**

- Allure:
  > Com o node instalado, execute o comando: <br>
  > **npm install allure** <br>
  > Execute o comando na raiz do projeto: <br>
  > **allure serve .\target\allure-results\\** <br>

- Sonar:
  > Abaixo temos uma orientação para rodar no sonar, basta subir o docker compose na raiz e executar os passos abaixo.

### REGRAS DE NEGÓCIO
- Para cadas reserva efetuada, por padrão temos uma variável de ambiente chamada **"tolerancia-mesa"**.
- Essa variável serve para definir um tempo padrão para um cliente ficar na mesa, por exemplo:
- Reservei uma mesa para as 14hs, então a data de saída padrão será às 16hs.
- É necessário isso, para impedir que um novo cliente tente reservar a mesa às 14hs01min por exemplo.

### ARQUITETURA
- Linguagem: **Java 17**
- Framework: **Spring Boot**
- Banco de dados: **Postgres**
- Testes e Perfomance: **Gatling**

### FLUXO
- Abra o postman 
- importe a Collection: /collections/Restaurante.postman_collection.json
- Para alguns cadastros, precisamos de alguns dados no sistemas já existentes, por exemplo:
- Não dá pra cadastrar uma reserva sem ao menos existir um restaurante e um usuário.
- Podemos cadastrar dados iniciais na seguinte ordem:
  > **Cadastrar Usuário**<br>
  > **Cadastrar Restaurante**
- Executando a chamada pelo postman da aplicação, automaticamente o sistema irá gerar a período por default de
- horário de funcionamento de segunda-feira a sexta-feira e os feriados nacionais.

### INSTALAÇÃO
- Como dito acima, para iniciar a execução do microserviço de restaurante, precisamos ter configurado o Docker.
- Configurar o arquivo na raíz **checkstyle.xml** usando Plugin **CheckStyle-IDEA**.
- Instalar o Postman para testar as requisições.
- Deve ser executado na raíz: **/docker-compose.yaml**
- Em seguida, apontar o Enviroments Variables para o arquivo: **/env/variaveis.env**

### SUBIR COM DOCKER COMPOSE
- Para subir a imagem basta executar o **docker-compose.yaml**

### SWAGGER
- **Swagger**: http://localhost:8080/swagger-ui/index.html#/

# DICAS

### SONAR: <br>
> Crie um projeto local no sonar através da URL: **http://localhost:9000/projects**.<br>
> Após a criação será gerado o comando para execução, utilize o comando maven, exemplo:<br><br>

> **mvn clean verify sonar:sonar -Dsonar.projectKey=restaurante -Dsonar.projectName="restaurante" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_691089050af08e36f15bcc69185c78248f6edca7 -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml** <br><br>

> Exeutando o comando, será gerado a url para verificar a cobertura do Sonar.<br>

