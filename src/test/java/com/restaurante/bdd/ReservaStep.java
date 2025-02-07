package com.restaurante.bdd;

import com.restaurante.app.rest.request.ReservaRequest;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ReservaStep extends BaseUnitTest {

    private Response response;

    private ReservaRequest request;

    private ReservaDTO dto;

    private final String ENDPOINT = "http://localhost:8080/reserva";

    // Salvar
    @Quando("submeter uma nova Reserva")
    public ReservaDTO submeterUmaNovaReserva() {
        request = gerarNovaReserva();
        request.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT);
        return response.then().extract().as(ReservaDTO.class);
    }

    @Então("a Reserva é salva com sucesso")
    public void aReservaSalvaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/reserva.json"));
    }

    // Atualizar
    @Dado("que uma Reserva já exista no sistema")
    public void queUmaReservaJaexistaNosistema() {
        dto = submeterUmaNovaReserva();
    }

    @Quando("requisitar a alteração da Reserva")
    public void requisitarAlteracaoDaReserva() {
        dto.setStatusPagamento(StatusPagamentoEnum.PAGO);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put(ENDPOINT + "/{id}", dto.getId().toString());
    }

    @Então("a Reserva é atualizada com sucesso")
    public void aReservaAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/reserva.json"));
    }


    @Dado("que um Reserva já exista")
    public void queUmaReservaJaexista() {
        dto = submeterUmaNovaReserva();
    }

    @Quando("requisitar a busca da Reserva")
    public void requisitarBuscaDareserva() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto).when()
                .get(ENDPOINT + "/1");
    }

    @Então("a Reserva é exibida com sucesso")
    public void aReservaExibidaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/reserva-list.json"));
    }

    private ReservaRequest gerarNovaReserva() {
        ReservaRequest requestRandom = new ReservaRequest();
        requestRandom.setDataDaReserva(LocalDate.now().atTime(10, 0));
        requestRandom.setDataFimReserva(LocalDate.now().atTime(11, 0));
        requestRandom.setStatusReserva(StatusReservaEnum.RESERVADO);
        requestRandom.setValorReserva(BigDecimal.valueOf(100));
        requestRandom.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        requestRandom.setUsuarioId(1L);
        requestRandom.setMesaId(1L);
        requestRandom.setRestauranteId(1L);
        return requestRandom;
    }

}