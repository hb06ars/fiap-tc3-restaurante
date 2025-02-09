package com.restaurante.perfomance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ApiPerformanceSimulationReserva extends Simulation {

    private final String ENDPOINT = "http://localhost:8080/reserva";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder adicionarRequest = adicionarRequest();
    ActionBuilder atualizarRequest = atualizarRequest();
    ActionBuilder buscarRequest = buscarRequest();


    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder cenario = scenario("Reserva")
            .exec(adicionarRequest).pause(1)
            .exec(atualizarRequest).pause(1)
            .exec(buscarRequest).pause(1);


    // Setup ----------------------------------------------------------------------
    {
        setUp(
                cenario.injectOpen(
                        constantUsersPerSec(1).during(5)
                )


        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(2000),
                        global().failedRequests().count().is(0L)
                );
    }


    private static HttpRequestActionBuilder adicionarRequest() {
        return http("Adicionar reserva")
                .post("")
                .body(StringBody(session -> {

                    return "{\n" +
                            "    \"usuarioId\": 1,\n" +
                            "    \"mesaId\": 1,\n" +
                            "    \"restauranteId\": 1,\n" +
                            "    \"dataDaReserva\": \"2099-02-02T08:00:00\",\n" +
                            "    \"dataFimReserva\": \"2099-02-02T09:00:00\",\n" +
                            "    \"valorReserva\": 100.00,\n" +
                            "    \"statusPagamento\": \"PENDENTE\",\n" +
                            "    \"statusReserva\": \"RESERVADO\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists())
                .check(jsonPath("$.id").saveAs("id"));
    }

    private static HttpRequestActionBuilder atualizarRequest() {
        return http("Atualizar reserva")
                .put("/#{id}")
                .body(StringBody(session -> {

                    return "{\n" +
                            "    \"usuarioId\": 1,\n" +
                            "    \"mesaId\": 1,\n" +
                            "    \"restauranteId\": 1,\n" +
                            "    \"dataDaReserva\": \"2099-02-02T08:00:00\",\n" +
                            "    \"dataFimReserva\": \"2099-02-02T09:00:00\",\n" +
                            "    \"valorReserva\": 100.00,\n" +
                            "    \"statusPagamento\": \"PENDENTE\",\n" +
                            "    \"statusReserva\": \"RESERVADO\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").saveAs("id"));
    }

    private static HttpRequestActionBuilder buscarRequest() {
        return http("Buscar reserva")
                .get("/#{id}")
                .body(StringBody(session -> {
                    return "{\n" +
                            "    \"dataDaReserva\": \"2099-02-02T08:10:00\",\n" +
                            "    \"statusPagamento\": \"PENDENTE\",\n" +
                            "    \"statusReserva\": \"RESERVADO\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200));
    }


}
