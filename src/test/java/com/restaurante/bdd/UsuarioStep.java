package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class UsuarioStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOINT = "http://localhost:8080/usuario";


    @Quando("submeter um novo Usuário")
    public void submeter_um_novo_usuário() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Usuário é salvo com sucesso")
    public void o_usuário_é_salvo_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Usuário já exista no sistema")
    public void que_um_usuário_já_exista_no_sistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração do Usuário")
    public void requisitar_a_alteração_do_usuário() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Usuário é atualizado com sucesso")
    public void o_usuário_é_atualizado_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Usuário já exista")
    public void que_um_usuário_já_exista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca do Usuário")
    public void requisitar_a_busca_do_usuário() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Usuário é exibido com sucesso")
    public void o_usuário_é_exibido_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}