package com.restaurante.perfomance;

import com.restaurante.domain.enums.TipoCozinhaEnum;
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

public class ApiPerformanceSimulationRestaurante extends Simulation {
    private final String ENDPOINT = "http://localhost:8080/restaurante";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT)
            .header("Content-Type", "application/json");

    // Criando as requisições ----------------------------------------------
    ActionBuilder adicionarRequest = adicionarRequest();
    ActionBuilder atualizarRequest = atualizarRequest();
    ActionBuilder buscarRequest = buscarRequest();

    // Criando cenários ----------------------------------------------
    ScenarioBuilder cenarioAdicionar = scenario("Adicionar restaurante")
            .exec(adicionarRequest);

    ScenarioBuilder cenarioAtualizar = scenario("Adicionar e Atualizar restaurante")
            .exec(adicionarRequest)
            .exec(atualizarRequest);

    ScenarioBuilder cenarioAdicionarBuscar = scenario("Adicionar e Buscar restaurante")
            .exec(adicionarRequest)
            .exec(buscarRequest);

    // Setup ----------------------------------------------
    {
        setUp(
                cenarioAdicionar.injectOpen(
                        rampUsersPerSec(1).to(5).during(Duration.ofSeconds(3)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(4)),
                        rampUsersPerSec(5).to(1).during(Duration.ofSeconds(3))
                ),
                cenarioAtualizar.injectOpen(
                        rampUsersPerSec(1).to(5).during(Duration.ofSeconds(3)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(4)),
                        rampUsersPerSec(5).to(1).during(Duration.ofSeconds(3))
                ),
                cenarioAdicionarBuscar.injectOpen(
                        rampUsersPerSec(1).to(5).during(Duration.ofSeconds(3)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(4)),
                        rampUsersPerSec(5).to(1).during(Duration.ofSeconds(3))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(2500),
                        global().failedRequests().count().is(0L)
                );
    }

    private static HttpRequestActionBuilder adicionarRequest() {
        return http("Adicionar restaurante")
                .post("")
                .body(StringBody(session -> {
                    String localizacao = "Av. " + UUID.randomUUID();
                    String tipoCozinha = TipoCozinhaEnum.BRASILEIRA.name();
                    String nomeRestaurante = "Restaurante_" + UUID.randomUUID();

                    return "{\n" +
                            "    \"nome\": \"" + nomeRestaurante + "\",\n" +
                            "    \"tipoCozinha\": \"" + tipoCozinha + "\",\n" +
                            "    \"localizacao\": \"" + localizacao + "\",\n" +
                            "    \"capacidade\": 50\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").saveAs("id"))
                .check(jsonPath("$.nome").saveAs("nome"));
    }

    private static HttpRequestActionBuilder atualizarRequest() {
        return http("Atualizar restaurante")
                .put("/#{id}")
                .body(StringBody(session -> {
                    String localizacao = "Av. " + UUID.randomUUID();
                    String tipoCozinha = TipoCozinhaEnum.BRASILEIRA.name();

                    return "{\n" +
                            "    \"nome\": \"Restaurante Atualizado\",\n" +
                            "    \"tipoCozinha\": \"" + tipoCozinha + "\",\n" +
                            "    \"localizacao\": \"" + localizacao + "\",\n" +
                            "    \"capacidade\": 60\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists());
    }

    private static HttpRequestActionBuilder buscarRequest() {
        return http("Buscar restaurante")
                .get("?nome=#{nome}")
                .check(status().is(200));
    }
}
