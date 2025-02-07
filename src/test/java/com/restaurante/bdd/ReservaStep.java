package com.restaurante.bdd;

import com.restaurante.app.rest.request.ReservaRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class ReservaStep extends BaseUnitTest {

    private Response response;

    private ReservaRequest request;

    private final String ENDPOINT = "http://localhost:8080/reserva";


    @Quando("submeter uma nova Reserva")
    public void submeterUmaNovareserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é salva com sucesso")
    public void aReservaSalvaComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que uma Reserva já exista no sistema")
    public void queUmaReservaJaexistaNosistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração da Reserva")
    public void requisitarAlteracaoDaReserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é atualizado com sucesso")
    public void aReservaAtualizadoComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Reserva já exista")
    public void queUmaReservaJaexista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Reserva")
    public void requisitarBuscaDareserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é exibida com sucesso")
    public void aReservaExibidaComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}