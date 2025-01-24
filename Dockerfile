// Fazendo o build
FROM maven:3.8.4-openjdk-17-slim AS build

// Cria um diretorio chamado app
WORKDIR . /app

// Copia tudo que tem na estrutura para dentro do diretorio
WORKDIR . /app

// Para que ele esteja gerando pacote de nossa aplicação
RUN mvn package

FROM openjdk:17-jdk-slim

// Fazendo a cópia do build do target do java
COPY --from=build /app/target/*.jar /app/app.jar
