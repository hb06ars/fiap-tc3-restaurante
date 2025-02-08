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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final String ENDPOINT = "http://localhost:8080/reserva";
    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(ENDPOINT).header("Content-Type", "application/json");

    // Builder ----------------------------------------------------------------------
    ActionBuilder adicionarRequest = adicionarRequest();
    ActionBuilder atualizarRequest = atualizarRequest();
    ActionBuilder buscarRequest = buscarRequest();


    // Cenários ----------------------------------------------------------------------
    ScenarioBuilder cenarioAdicionar = scenario("Adicionar reserva").exec(adicionarRequest);

    ScenarioBuilder cenarioAtualizar = scenario("Adicionar e Atualizar reserva")
            .exec(adicionarRequest).exec(atualizarRequest);

    ScenarioBuilder cenarioAdicionarBuscar = scenario("Adicionar e Buscar reserva")
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
                        global().responseTime().max().lt(1200),
                        global().failedRequests().count().is(0L)
                );
    }


    private static HttpRequestActionBuilder adicionarRequest() {
        return http("Adicionar reserva")
                .post("")
                .body(StringBody(session -> {
                    String dataInicioReserva = LocalDateTime.now().format(formatter);
                    String dataFimReserva = LocalDateTime.now().plusHours(1).format(formatter);

                    return "{\n" +
                            "    \"usuarioId\": 1,\n" +
                            "    \"mesaId\": 1,\n" +
                            "    \"restauranteId\": 1,\n" +
                            "    \"dataDaReserva\": " + dataInicioReserva + ",\n" +
                            "    \"dataFimReserva\": " + dataFimReserva + ",\n" +
                            "    \"valorReserva\": 100.00,\n" +
                            "    \"statusPagamento\": \"PENDENTE\",\n" +
                            "    \"statusReserva\": \"RESERVADO\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists());
    }

    private static HttpRequestActionBuilder atualizarRequest() {
        return http("Atualizar reserva")
                .put("/1")
                .body(StringBody(session -> {
                    String dataInicioReserva = LocalDate.now().atTime(10, 0).toString();
                    String dataFimReserva = LocalDate.now().atTime(11, 0).toString();

                    return "{\n" +
                            "    \"usuarioId\": 1,\n" +
                            "    \"mesaId\": 1,\n" +
                            "    \"restauranteId\": 1,\n" +
                            "    \"dataDaReserva\": " + dataInicioReserva + ",\n" +
                            "    \"dataFimReserva\": " + dataFimReserva + ",\n" +
                            "    \"valorReserva\": 100.00,\n" +
                            "    \"statusPagamento\": \"PENDENTE\",\n" +
                            "    \"statusReserva\": \"RESERVADO\"\n" +
                            "}";
                })).asJson()
                .check(status().is(200))
                .check(jsonPath("$.id").exists());
    }

    private static HttpRequestActionBuilder buscarRequest() {
        return http("Buscar reserva")
                .put("/1")
                .check(status().is(200));
    }


}
