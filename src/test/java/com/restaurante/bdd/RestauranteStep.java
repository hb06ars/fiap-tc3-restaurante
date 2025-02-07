package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class RestauranteStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOINT = "http://localhost:8080/restaurante";


    @Quando("submeter um novo Restaurante")
    public void submeter_um_novo_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Restaurante é salvo com sucesso")
    public void o_restaurante_é_salvo_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Restaurante já exista no sistema")
    public void que_um_restaurante_já_exista_no_sistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração do Restaurante")
    public void requisitar_a_alteração_do_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Restaurante é atualizado com sucesso")
    public void o_restaurante_é_atualizado_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Restaurante já foi salvo")
    public void que_um_restaurante_já_foi_salvo() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a exclusão do Restaurante")
    public void requisitar_a_exclusão_do_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Restaurante é removido com sucesso")
    public void o_restaurante_é_removido_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Restaurante já exista")
    public void que_um_restaurante_já_exista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca do Restaurante")
    public void requisitar_a_busca_do_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Restaurante é exibido com sucesso")
    public void o_restaurante_é_exibido_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}