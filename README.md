# RESTAURANTE

### SUBIR APLICAÇÃO
- Deve ser executado: [docker-compose.yaml](docker-compose.yaml)
- Em seguida, apontar o Enviroments Variables para o arquivo: [variaveis.env](env%2Fvariaveis.env)
- Por fim, aguarde subir a imagem e assim iniciar a aplicação.

### REGRAS
- Definindo regras de negócio aqui

### ARQUITETURA
- Linguagem: Java 17
- Framework: Spring Boot
- Banco de dados: a definir.

### FLUXO
- Forma e como é feito o fluxo.

### INSTALAÇÃO
- Dicas de como subir e se necessário instalar algo.

### DOCKER COMPOSE
- Para subir a imagem basta executar o docker-compose.yaml

### SWAGGER
- Swagger: http://localhost:8080/swagger-ui/index.html#/

# DICAS

### SONAR: <br>
    Crie um projeto local no sonar através da URL: http://localhost:9000/projects.<br>
    Após a criação será gerado o comando para execução, utilize o comando maven, exemplo:<br>
    mvn clean verify sonar:sonar -Dsonar.projectKey=restaurante -Dsonar.projectName="restaurante" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_691089050af08e36f15bcc69185c78248f6edca7 
    <br>Exeutando o comando, será gerado a url para verificar a cobertura do Sonar.

### JMETER: <br>
    Na pasta: /src/test/jmeter há um arquivo padrão para realizar testes de stress em algumas requisições.
    Baixe o jmeter no site: https://jmeter.apache.org/download_jmeter.cgi e siga as instruções no jMeter.txt
