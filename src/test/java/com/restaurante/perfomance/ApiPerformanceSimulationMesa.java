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

public class ApiPerformanceSimulationMesa extends Simulation {
    private final String ENDPOINT = "http://localhost:8080/mesa";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder gerenciarRequest = gerenciarMesaRequest();


    
    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder gerenciarMesa = scenario("Gerenciar mesa").exec(gerenciarRequest);



    // Setup ----------------------------------------------------------------------
    {
        setUp(
                gerenciarMesa.injectOpen(
                        rampUsersPerSec(1).to(10).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10).during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10).to(1).during(Duration.ofSeconds(10))
                )

        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(800),
                        global().failedRequests().count().is(0L)
                );
    }

    private static HttpRequestActionBuilder gerenciarMesaRequest() {
        return http("Buscar mesa")
                .get("/1")
                .check(jsonPath("$.mesaId").exists())
                .check(status().is(200));
    }


}
