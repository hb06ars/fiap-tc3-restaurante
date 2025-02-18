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

public class ApiPerformanceSimulationFuncionamento extends Simulation {
    private final String ENDPOINT = "http://localhost:8080/funcionamento";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder adicionarRequest = adicionarRequest();
    ActionBuilder atualizarRequest = atualizarRequest();
    ActionBuilder buscarRequest = buscarRequest();


    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder cenarioAdicionar = scenario("Adicionar funcionamento").exec(adicionarRequest);

    ScenarioBuilder cenarioAtualizar = scenario("Adicionar e Atualizar funcionamento")
            .exec(adicionarRequest).exec(atualizarRequest);

    ScenarioBuilder cenarioAdicionarBuscar = scenario("Adicionar e Buscar funcionamento")
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
                        global().responseTime().max().lt(2000),
                        global().failedRequests().count().is(0L)
                );
    }


    private static HttpRequestActionBuilder adicionarRequest() {
        return http("Adicionar funcionamento")
                .post("/cadastrar")
                .body(StringBody(session -> {
                    String email = "fulano" + UUID.randomUUID() + "@mail.com";
                    String celular = "119" + (int) (Math.random() * 100000000);

                    return "{\n" +
                            "    \"diaEnum\": \"SABADO\",\n" +
                            "    \"abertura\": \"14:00:00\",\n" +
                            "    \"fechamento\": \"20:00:00\",\n" +
                            "    \"restauranteId\": 1\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists())
                .check(jsonPath("$.restauranteId").saveAs("restauranteId"));
    }

    private static HttpRequestActionBuilder atualizarRequest() {
        return http("Atualizar funcionamento")
                .put("/atualizar/#{restauranteId}")
                .body(StringBody(session -> {
                    String email = "fulanoNovo" + UUID.randomUUID() + "@mail.com";
                    String celular = "119" + (int) (Math.random() * 100000000);

                    return "{\n" +
                            "    \"diaEnum\": \"SABADO\",\n" +
                            "    \"abertura\": \"14:00:00\",\n" +
                            "    \"fechamento\": \"20:00:00\",\n" +
                            "    \"restauranteId\": 1\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists())
                .check(jsonPath("$.restauranteId").saveAs("restauranteId"));
    }

    private static HttpRequestActionBuilder buscarRequest() {
        return http("Buscar funcionamento")
                .get("/1")
                .check(status().is(200));
    }


}