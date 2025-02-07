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

    private final String ENDPOINT = "http://localhost:8080/mesa";

    @Quando("submeter uma nova Mesa")
    public MesaDTO submeterUmaNovaMesa() {
        MesaRequest request = gerarNovaMesa();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT + "/cadastrar");
        return response.then().extract().as(MesaDTO.class);
    }

    @Então("a Mesa é salva com sucesso")
    public void aMesaSalvaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/mesa.json"));
    }


    @Dado("que um Mesa já exista no sistema")
    public void queUmMesaJaExistaNoSistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração da Mesa")
    public void requisitarAlteracaoDaMesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Mesa é atualizada com sucesso")
    public void aMesaAtualizadaComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Mesa já exista")
    public void queUmaMesaJaExista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Mesa")
    public void requisitarBuscaDaMesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Mesa é exibida com sucesso")
    public void aMesaExibidaComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Mesa já exista para o restaurante")
    public void queUmaMesaJaExistaParaRestaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Mesa pelo Id do Restaurante")
    public void requisitarAbuscaDaMesaPeloIdDoEestaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("as Mesas são exibidas com sucesso")
    public void asMesasSaoExibidasComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    private MesaRequest gerarNovaMesa() {
        MesaRequest requestRandom = new MesaRequest();
        requestRandom.setNomeMesa("MESA 1");
        requestRandom.setRestauranteId(1L);
        return requestRandom;
    }
}