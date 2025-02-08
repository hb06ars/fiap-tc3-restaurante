package com.restaurante.perfomance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ApiPerformanceSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .header("Content-Type", "application/json");

    ActionBuilder adicionarMensagemRequest = http("Adicionar usuário")
            .post("/usuario/cadastrar")
            .body(StringBody("{\n" +
                    "    \"nome\": \"Fulano de Tald\",\n" +
                    "    \"email\": \"fulano@mail.com\",\n" +
                    "    \"celular\": \"11988887777\"\n" +
                    "}")).asJson()
            .check(status().is(200))
            .check(jsonPath("$.id").exists());

    ScenarioBuilder cenarioAdicionarMensagem = scenario("Adicionar usuário")
            .exec(adicionarMensagemRequest);

    {
        setUp(
                cenarioAdicionarMensagem.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10).to(1).during(Duration.ofSeconds(10))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(50),
                        global().failedRequests().count().is(0L)
                );
    }
}
