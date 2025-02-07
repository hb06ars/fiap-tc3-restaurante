package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class ReservaStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOINT = "http://localhost:8080/reserva";


    @Quando("submeter uma nova Reserva")
    public void submeter_uma_nova_reserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é salva com sucesso")
    public void a_reserva_é_salva_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que uma Reserva já exista no sistema")
    public void que_uma_reserva_já_exista_no_sistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração da Reserva")
    public void requisitar_a_alteração_da_reserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é atualizado com sucesso")
    public void a_reserva_é_atualizado_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Reserva já exista")
    public void que_um_reserva_já_exista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Reserva")
    public void requisitar_a_busca_da_reserva() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Reserva é exibida com sucesso")
    public void a_reserva_é_exibida_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}