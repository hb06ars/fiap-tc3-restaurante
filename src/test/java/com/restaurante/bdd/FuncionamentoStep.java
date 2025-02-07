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
    public FuncionamentoDTO submeterUmNovoHorarioDeFuncionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é salvo com sucesso")
    public void oHorarioDeFuncionamentoSalvoComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já exista no sistema")
    public void queUmHorarioDefuncionamentoJaExistaNosistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração do Horário de Funcionamento")
    public void requisitarAlteracaoDoHorarioDefuncionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é atualizado com sucesso")
    public void oHorarioDefuncionamentoAtualizadoComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já foi salvo")
    public void queUmHorarioDefuncionamentoJafoiSalvo() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a exclusão do Horário de Funcionamento")
    public void requisitarexclusaoDohorarioDefuncionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é removido com sucesso")
    public void oHorarioDefuncionamentoremovidoComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Dado("que um Horário de Funcionamento já exista")
    public void queUmHorarioDefuncionamentoJaexista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca do Horário de Funcionamento")
    public void requisitarbuscaDohorarioDefuncionamento() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("o Horário de Funcionamento é exibido com sucesso")
    public void oHorarioDefuncionamentoexibidoComSucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}