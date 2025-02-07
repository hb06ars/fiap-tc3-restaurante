package com.restaurante.bdd;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.it.Quando;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.restassured.response.Response;

public class MesaStep extends BaseUnitTest {

    private Response response;

    private FuncionamentoRequest request;

    private final String ENDPOINT = "http://localhost:8080/mesa";

    @Quando("submeter uma nova Mesa")
    public void submeter_uma_nova_mesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("ao Mesa é salva com sucesso")
    public void ao_mesa_é_salva_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }




    @Dado("que um Mesa já exista no sistema")
    public void que_um_mesa_já_exista_no_sistema() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a alteração da Mesa")
    public void requisitar_a_alteração_da_mesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Mesa é atualizada com sucesso")
    public void a_mesa_é_atualizada_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }





    @Dado("que um Mesa já exista")
    public void que_um_mesa_já_exista() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Mesa")
    public void requisitar_a_busca_da_mesa() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a Mesa é exibida com sucesso")
    public void a_mesa_é_exibida_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }






    @Dado("que um Mesa já exista para o restaurante")
    public void que_um_mesa_já_exista_para_o_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Quando("requisitar a busca da Mesa pelo Id do Restaurante")
    public void requisitar_a_busca_da_mesa_pelo_id_do_restaurante() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("as Mesas são exibidas com sucesso")
    public void as_mesas_sao_exibidas_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}