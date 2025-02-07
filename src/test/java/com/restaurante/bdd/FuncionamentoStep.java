package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class FuncionamentoStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOINT = "http://localhost:8080/funcionamento";

    private FuncionamentoDTO dto;

    // Cadastrar
    @Quando("submeter um novo Horário de Funcionamento")
    public FuncionamentoDTO submeterUmNovoHorarioDeFuncionamento() {
        request = gerarNovoFuncionamento();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT + "/cadastrar");

        return response.then().extract().as(FuncionamentoDTO.class);
    }

    @Então("o Horário de Funcionamento é salvo com sucesso")
    public void oHorarioDeFuncionamentoSalvoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/funcionamento.json"));
    }


    @Dado("que um Horário de Funcionamento já exista no sistema")
    public void queUmHorarioDefuncionamentoJaExistaNosistema() {
        dto = submeterUmNovoHorarioDeFuncionamento();
    }

    @Quando("requisitar a alteração do Horário de Funcionamento")
    public void requisitarAlteracaoDoHorarioDefuncionamento() {
        dto.setFechamento(LocalTime.of(19, 0));
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put(ENDPOINT + "/atualizar/1");
    }

    @Então("o Horário de Funcionamento é atualizado com sucesso")
    public void oHorarioDefuncionamentoAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/funcionamento.json"));
    }


    @Dado("que um Horário de Funcionamento já foi salvo")
    public void queUmHorarioDefuncionamentoJafoiSalvo() {
        dto = submeterUmNovoHorarioDeFuncionamento();
    }

    @Quando("requisitar a exclusão do Horário de Funcionamento")
    public void requisitarexclusaoDohorarioDefuncionamento() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT + "/deletar/1");
    }

    @Então("o Horário de Funcionamento é removido com sucesso")
    public void oHorarioDefuncionamentoremovidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("{\"mensagem\":\"Registro removido com sucesso\"}"));
    }


    @Dado("que um Horário de Funcionamento já exista")
    public void queUmHorarioDefuncionamentoJaexista() {
        dto = submeterUmNovoHorarioDeFuncionamento();
    }

    @Quando("requisitar a busca do Horário de Funcionamento")
    public void requisitarbuscaDohorarioDefuncionamento() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto).when()
                .get(ENDPOINT + "/1");
    }

    @Então("o Horário de Funcionamento é exibido com sucesso")
    public void oHorarioDefuncionamentoexibidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/funcionamento-list.json"));
    }

    private FuncionamentoRequest gerarNovoFuncionamento() {
        var diaAtual = LocalDate.now().getDayOfWeek().getValue();
        FuncionamentoRequest requestRandom = new FuncionamentoRequest();
        requestRandom.setAbertura(LocalTime.of(8, 0));
        requestRandom.setFechamento(LocalTime.of(22, 0));
        requestRandom.setDiaEnum(DiaEnum.fromInt(diaAtual));
        requestRandom.setRestauranteId(1L);
        return requestRandom;
    }
}