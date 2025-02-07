package com.restaurante.bdd;

import com.restaurante.app.rest.request.MesaRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.response.Response;

public class MesaStep extends BaseUnitTest {

    private Response response;

    private MesaRequest request;

    private final String ENDPOINT = "http://localhost:8080/mesa";

    @Quando("submeter uma nova Mesa")
    public void submeterUmaNovaMesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Mesa é salva com sucesso")
    public void aMesaSalvaComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
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


}