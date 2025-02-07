package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class FuncionamentoStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOIND = "http://localhost:8080/funcionamento";


    @Quando("submeter um novo Horário de Funcionamento")
    public FuncionamentoDTO submeter_um_novo_horário_de_funcionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é salvo com sucesso")
    public void o_horário_de_funcionamento_é_salvo_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já exista no sistema")
    public void que_um_horário_de_funcionamento_já_exista_no_sistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração do Horário de Funcionamento")
    public void requisitar_a_alteração_do_horário_de_funcionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é atualizado com sucesso")
    public void o_horário_de_funcionamento_é_atualizado_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já foi salvo")
    public void que_um_horário_de_funcionamento_já_foi_salvo() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a exclusão do Horário de Funcionamento")
    public void requisitar_a_exclusão_do_horário_de_funcionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é removido com sucesso")
    public void o_horário_de_funcionamento_é_removido_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já exista")
    public void que_um_horário_de_funcionamento_já_exista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca do Horário de Funcionamento")
    public void requisitar_a_busca_do_horário_de_funcionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é exibido com sucesso")
    public void o_horário_de_funcionamento_é_exibido_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}