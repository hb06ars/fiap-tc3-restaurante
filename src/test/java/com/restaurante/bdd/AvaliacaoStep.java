package com.restaurante.bdd;

import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AvaliacaoStep extends BaseUnitTest {

    private Response response;

    private AvaliacaoRequest request;

    private final String ENDPOIND = "http://localhost:8080/avaliacao";

    @Quando("submeter uma nova mensagem")
    public AvaliacaoDTO submeter_uma_nova_mensagem() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("a mensagem é registrada com sucesso")
    public void a_mensagem_é_registrada_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    @Quando("requisitar a lista da mensagem")
    public void requisitarListaMensagens() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Então("as avaliações sao exibidas com sucesso")
    public void as_avaliações_sao_exibidas_com_sucesso() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}