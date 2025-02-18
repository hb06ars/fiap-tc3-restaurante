package com.restaurante.perfomance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ApiPerformanceSimulationMesa extends Simulation {
    private final String ENDPOINT = "http://localhost:8080/mesa";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder gerenciarRequest = gerenciarMesaRequest();
    ActionBuilder gerenciarDisponiveisRequest = gerenciarDisponiveisMesaRequest();


    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder gerenciarMesa = scenario("Encontrar todas as mesas").exec(gerenciarRequest);
    ScenarioBuilder gerenciarMesaDisponivel = scenario("Encontrar mesas disponiveis").exec(gerenciarDisponiveisRequest);


    // Setup ----------------------------------------------------------------------
    {
        setUp(
                gerenciarMesa.injectOpen(
                        rampUsersPerSec(1).to(5).during(Duration.ofSeconds(3)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(4)),
                        rampUsersPerSec(5).to(1).during(Duration.ofSeconds(3))

                ),
                gerenciarMesaDisponivel.injectOpen(
                        rampUsersPerSec(1).to(5).during(Duration.ofSeconds(3)),
                        constantUsersPerSec(5).during(Duration.ofSeconds(4)),
                        rampUsersPerSec(5).to(1).during(Duration.ofSeconds(3))

                )

        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(2000),
                        global().failedRequests().count().is(0L)
                );
    }

    private static HttpRequestActionBuilder gerenciarMesaRequest() {
        return http("Buscar todas as mesas pelo restaurante")
                .get("/1")
                .check(status().is(200));
    }

    private static HttpRequestActionBuilder gerenciarDisponiveisMesaRequest() {
        return http("Buscar apenas as mesas disponiveis pelo restaurante")
                .get("/listaporrestaurante/1")
                .check(status().is(200));
    }


}
