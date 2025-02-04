package com.restaurante.app.rest.controller;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.utils.BaseUnitTest;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FuncionamentoControllerTest extends BaseUnitTest {

    private MockMvc mockMvc;

    @Mock
    private FuncionamentoService funcionamentoService;

    private FuncionamentoDTO funcionamentoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        funcionamentoDTO = new FuncionamentoDTO();
        funcionamentoDTO.setId(1L);
        funcionamentoDTO.setDiaEnum(DiaEnum.SEGUNDA);
        funcionamentoDTO.setAbertura(LocalTime.of(8, 0, 1));
        funcionamentoDTO.setFechamento(LocalTime.of(18, 0, 1));
        funcionamentoDTO.setRestauranteId(1L);

        FuncionamentoController funcionamentoController = new FuncionamentoController(funcionamentoService);
        mockMvc = MockMvcBuilders.standaloneSetup(funcionamentoController).build();
    }

    @Test
    void testCadastrarFuncionamento() throws Exception {
        when(funcionamentoService.save(any(FuncionamentoDTO.class))).thenReturn(funcionamentoDTO);

        mockMvc.perform(post("/funcionamento/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"diaEnum\":\"SEGUNDA\", \"abertura\":\"08:00:01\", " +
                                "\"fechamento\":\"18:00:01\", " +
                                "\"restauranteId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.diaEnum").value(DiaEnum.SEGUNDA.name()))
                .andExpect(jsonPath("$.abertura[0]").value(8))
                .andExpect(jsonPath("$.abertura[1]").value(0))
                .andExpect(jsonPath("$.abertura[2]").value(1))
                .andExpect(jsonPath("$.fechamento[0]").value(18))
                .andExpect(jsonPath("$.fechamento[1]").value(0))
                .andExpect(jsonPath("$.fechamento[2]").value(1))
                .andExpect(jsonPath("$.restauranteId").value(1));
    }

    @Test
    void testAtualizarFuncionamento() throws Exception {
        when(funcionamentoService.update(any(Long.class), any(FuncionamentoDTO.class))).thenReturn(funcionamentoDTO);

        mockMvc.perform(put("/funcionamento/atualizar/{idFuncionamento}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"diaEnum\":\"SEGUNDA\", \"abertura\":\"08:00:01\", " +
                                "\"fechamento\":\"18:00:01\", \"restauranteId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.diaEnum").value(DiaEnum.SEGUNDA.name()))
                .andExpect(jsonPath("$.abertura[0]").value(8))
                .andExpect(jsonPath("$.abertura[1]").value(0))
                .andExpect(jsonPath("$.abertura[2]").value(1))
                .andExpect(jsonPath("$.fechamento[0]").value(18))
                .andExpect(jsonPath("$.fechamento[1]").value(0))
                .andExpect(jsonPath("$.fechamento[2]").value(1))
                .andExpect(jsonPath("$.restauranteId").value(1));
    }


    @Test
    void testAvaliarExcecaoQuandoIdNaoEncontrado() throws Exception {
        mockMvc.perform(put("/funcionamento/atualizar/235")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getRandom(FuncionamentoDTO.class))))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarFuncionamento() throws Exception {
        mockMvc.perform(delete("/funcionamento/deletar/{idFuncionamento}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Registro removido com sucesso"));
    }

    @Test
    void testBuscarPorRestaurante() throws Exception {
        when(funcionamentoService.buscarPorRestaurante(1L))
                .thenReturn(Collections.singletonList(funcionamentoDTO));

        mockMvc.perform(get("/funcionamento/{idRestaurante}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].diaEnum").value(DiaEnum.SEGUNDA.name()))
                .andExpect(jsonPath("$[0].abertura[0]").value(8))
                .andExpect(jsonPath("$[0].abertura[1]").value(0))
                .andExpect(jsonPath("$[0].abertura[2]").value(1))
                .andExpect(jsonPath("$[0].fechamento[0]").value(18))
                .andExpect(jsonPath("$[0].fechamento[1]").value(0))
                .andExpect(jsonPath("$[0].fechamento[2]").value(1))
                .andExpect(jsonPath("$[0].restauranteId").value(1));
    }
}
