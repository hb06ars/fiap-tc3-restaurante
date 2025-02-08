package com.restaurante.perfomance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.time.Duration;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ApiPerformanceSimulation extends Simulation {
    private final String ENDPOINT = "http://localhost:8080/usuario";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder adicionarRequest = adicionarRequest();
    ActionBuilder atualizarRequest = atualizarRequest();
    ActionBuilder buscarRequest = buscarRequest();


    
    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder cenarioAdicionar = scenario("Adicionar usuário").exec(adicionarRequest);

    ScenarioBuilder cenarioAtualizar = scenario("Atualizar usuário").exec(atualizarRequest);

    ScenarioBuilder cenarioAdicionarBuscar = scenario("Adicionar e Buscar usuário")
            .exec(adicionarRequest).exec(buscarRequest);


    // Setup ----------------------------------------------------------------------
    {
        setUp(
                cenarioAdicionar.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10).to(1).during(Duration.ofSeconds(10))
                ),
                cenarioAtualizar.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10).to(1).during(Duration.ofSeconds(10))
                ),
                cenarioAdicionarBuscar.injectOpen(
                        rampUsersPerSec(1).to(30).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(30).during(Duration.ofSeconds(60)),
                        rampUsersPerSec(30).to(1).during(Duration.ofSeconds(10)))


        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(800),
                        global().failedRequests().count().is(0L)
                );
    }


    private static HttpRequestActionBuilder adicionarRequest() {
        return http("Adicionar usuário")
                .post("/cadastrar")
                .body(StringBody(session -> {
                    String email = "fulano" + UUID.randomUUID() + "@mail.com";
                    String celular = "119" + (int) (Math.random() * 100000000);

                    return "{\n" +
                            "    \"nome\": \"Fulano de Tal\",\n" +
                            "    \"email\": \"" + email + "\",\n" +
                            "    \"celular\": \"" + celular + "\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.celular").saveAs("celular"))
                .check(jsonPath("$.id").saveAs("id"));
    }

    private static HttpRequestActionBuilder atualizarRequest() {
        return http("Atualizar usuário")
                .post("/atualizar/#{id}")
                .body(StringBody(session -> {
                    String email = "fulanoNovo" + UUID.randomUUID() + "@mail.com";
                    String celular = "119" + (int) (Math.random() * 100000000);

                    return "{\n" +
                            "    \"nome\": \"Fulano de Tal Novo\",\n" +
                            "    \"email\": \"" + email + "\",\n" +
                            "    \"celular\": \"" + celular + "\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists());
    }

    private static HttpRequestActionBuilder buscarRequest() {
        return http("buscar usuário")
                .get("/usuario?celular=#{celular}")
                .check(status().is(200));
    }

}
