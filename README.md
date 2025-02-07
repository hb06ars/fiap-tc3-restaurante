# RESTAURANTE

### SUBIR APLICAÇÃO
- Deve ser executado: [docker-compose.yaml](docker-compose.yaml)
- Em seguida, apontar o Enviroments Variables para o arquivo: [variaveis.env](env%2Fvariaveis.env)
- Por fim, aguarde subir a imagem e assim iniciar a aplicação.

### TESTES
- Testes unitários:
  > mvn test

- Testes integrados:
  > mvn test -P integration-test -Dspring.profiles.active=test

- Testes de Sistema:
  > mvn test -P system-test -Dspring.profiles.active=test

- Cucumber:
  > Abra o arquivo: <seu-diretorio>\fiap-tc3-restaurante\target\cucumber-reports\cucumber.html

- Allure:
  - Com o node instalado, execute o comando:
  > npm install allure

  - Execute o comando na raiz do projeto:
  > allure serve .\target\allure-results\
 
- Sonar:
  > Abaixo temos uma orientação para rodar no sonar, basta subir o docker compose na raiz e executar os passos abaixo.

### REGRAS
- Para cadas reserva efetuada, por padrão temos uma variável de ambiente chamada "tolerancia-mesa".
- Essa variável serve para definir um tempo padrão para um cliente ficar na mesa, por exemplo:
- Reservei uma mesa para as 14hs, então a data de saída padrão será às 16hs.
- É necessário isso, para impedir que um novo cliente tente reservar a mesa às 14hs01min por exemplo.

### ARQUITETURA
- Linguagem: Java 17
- Framework: Spring Boot
- Banco de dados: Postgres.

### FLUXO
- Abra o postman 
- importe a Collection: [Restaurante.postman_collection.json](collections%2FRestaurante.postman_collection.json)
- Para alguns cadastros, precisamos de alguns dados no sistemas já existentes, por exemplo:
- Não dá pra cadastrar uma reserva sem ao menos existir um restaurante e um usuário.
- Podemos cadastrar dados iniciais na seguinte ordem:
  > Cadastrar Usuário<br>
  > Cadastrar Restaurante
- Executando a chamada pelo postman da aplicação, automaticamente o sistema irá gerar a período por default de
- horário de funcionamento de segunda-feira a sexta-feira e os feriados nacionais.

### INSTALAÇÃO
- Como dito acima, para iniciar a execução do microserviço de restaurante, precisamos ter configurado o Docker.
- Configurar o arquivo [checkstyle.xml](checkstyle.xml) usando Plugin CheckStyle-IDEA.
- Instalar o Postman para testar as requisições.
- Deve ser executado: [docker-compose.yaml](docker-compose.yaml)
- Em seguida, apontar o Enviroments Variables para o arquivo: [variaveis.env](env%2Fvariaveis.env)

### DOCKER COMPOSE
- Para subir a imagem basta executar o docker-compose.yaml

### SWAGGER
- Swagger: http://localhost:8080/swagger-ui/index.html#/

# DICAS

### SONAR: <br>
    Crie um projeto local no sonar através da URL: http://localhost:9000/projects.
    Após a criação será gerado o comando para execução, utilize o comando maven, exemplo:
    mvn clean verify
    mvn clean verify sonar:sonar -Dsonar.projectKey=restaurante -Dsonar.projectName="restaurante" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_691089050af08e36f15bcc69185c78248f6edca7 -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml 
    Exeutando o comando, será gerado a url para verificar a cobertura do Sonar.

### JMETER: <br>
    Na pasta: /src/test/jmeter há um arquivo padrão para realizar testes de stress em algumas requisições.
    Baixe o jmeter no site: https://jmeter.apache.org/download_jmeter.cgi e siga as instruções no jMeter.txt
