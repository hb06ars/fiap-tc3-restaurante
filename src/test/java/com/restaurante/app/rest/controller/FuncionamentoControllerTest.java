package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.utils.BaseUnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FuncionamentoControllerTest extends BaseUnitTest {

    AutoCloseable openMocks;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(FuncionamentoController.class);

    @Mock
    private FuncionamentoService service;

    private ObjectMapper objectMapper;

    private FuncionamentoDTO dto;
    private FuncionamentoRequest request;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        preencherObjetos();

        FuncionamentoController controller = new FuncionamentoController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private void preencherObjetos() {
        dto = new FuncionamentoDTO();
        dto.setId(1L);
        dto.setRestauranteId(1L);
        dto.setAbertura(LocalTime.of(8, 0));
        dto.setFechamento(LocalTime.of(18, 0));
        dto.setDiaEnum(DiaEnum.SEXTA);

        request = new FuncionamentoRequest();
        request.setId(dto.getId());
        request.setRestauranteId(dto.getRestauranteId());
        request.setAbertura(dto.getAbertura());
        request.setFechamento(dto.getFechamento());
        request.setDiaEnum(dto.getDiaEnum());
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testCadastrarFuncionamento() throws Exception {
        when(service.save(any(FuncionamentoDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/funcionamento/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"diaEnum\":\"SEXTA\", \"abertura\":\"08:00:01\", " +
                                "\"fechamento\":\"18:00:01\", " +
                                "\"restauranteId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.diaEnum").value(DiaEnum.SEXTA.name()))
                .andExpect(jsonPath("$.abertura[0]").value(8))
                .andExpect(jsonPath("$.fechamento[0]").value(18))
                .andExpect(jsonPath("$.restauranteId").value(1));
    }

    @Test
    void testAtualizarFuncionamento() throws Exception {
        when(service.update(any(Long.class), any(FuncionamentoDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/funcionamento/atualizar/{idFuncionamento}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"diaEnum\":\"SEXTA\", \"abertura\":\"08:00:01\", " +
                                "\"fechamento\":\"18:00:01\", \"restauranteId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.diaEnum").value(DiaEnum.SEXTA.name()))
                .andExpect(jsonPath("$.abertura[0]").value(8))
                .andExpect(jsonPath("$.fechamento[0]").value(18))
                .andExpect(jsonPath("$.restauranteId").value(1));
    }


    @Test
    void testAvaliarExcecaoQuandoIdNaoInformado() throws Exception {
        mockMvc.perform(put("/funcionamento/atualizar/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FuncionamentoRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAvaliarExcecaoQuandoIdNaoEncontrado() throws Exception {

        when(service.update(eq(999L), any(FuncionamentoDTO.class)))
                .thenThrow(new ObjectNotFoundException("Funcionamento 999 não encontrado."));

        mockMvc.perform(put("/funcionamento/atualizar/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0].erro").value("O objeto solicitado não foi encontrado no sistema"))
                .andExpect(jsonPath("$.message[0].detalhe").value("Funcionamento 999 não encontrado."));
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
        when(service.buscarPorRestaurante(1L))
                .thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/funcionamento/{idRestaurante}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].diaEnum").value(DiaEnum.SEXTA.name()))
                .andExpect(jsonPath("$[0].abertura[0]").value(8))
                .andExpect(jsonPath("$[0].fechamento[0]").value(18))
                .andExpect(jsonPath("$[0].restauranteId").value(1));
    }
}
