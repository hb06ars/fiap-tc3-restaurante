package com.restaurante.bdd;

import com.restaurante.app.rest.request.MesaRequest;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MesaStep extends BaseUnitTest {

    private Response response;

    private MesaRequest request;

    private MesaDTO dto;

    private final String ENDPOINT = "http://localhost:8080/mesa";

    // Atualizar
    @Dado("que uma Mesa já exista no sistema")
    public void queUmMesaJaExistaNoSistema() {
        dto = obterMesa();
    }

    @Quando("requisitar a alteração da Mesa")
    public void requisitarAlteracaoDaMesa() {
        dto.setNomeMesa("MESA A");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put(ENDPOINT + "/atualizar/1");
    }

    @Então("a Mesa é atualizada com sucesso")
    public void aMesaAtualizadaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/mesa.json"));
    }

    // Buscar Mesa disponpíveis por ID Restaurante
    @Dado("que uma Mesa já exista")
    public void queUmaMesaJaExista() {
        dto = obterMesa();
    }

    @Quando("requisitar a busca da Mesa")
    public void requisitarBuscaDaMesa() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto).when()
                .get(ENDPOINT + "/1");
    }

    @Então("a Mesa é exibida com sucesso")
    public void aMesaExibidaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/mesa-list.json"));
    }

    // Buscar Mesa por ID Restaurante
    @Dado("que uma Mesa já exista para o restaurante")
    public void queUmaMesaJaExistaParaRestaurante() {
        dto = obterMesa();
    }

    @Quando("requisitar a busca da Mesa pelo Id do Restaurante")
    public void requisitarAbuscaDaMesaPeloIdDoEestaurante() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto).when()
                .get(ENDPOINT + "/listaporrestaurante/1");
    }

    @Então("as Mesas são exibidas com sucesso")
    public void asMesasSaoExibidasComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/mesa-list.json"));
    }

    private MesaDTO obterMesa() {
        MesaDTO mesaDTO = new MesaDTO();
        mesaDTO.setId(1L);
        mesaDTO.setNomeMesa("MESA 1");
        mesaDTO.setRestauranteId(1L);
        return mesaDTO;
    }
}