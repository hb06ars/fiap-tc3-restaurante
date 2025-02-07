package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
import com.restaurante.utils.BaseUnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvaliacaoControllerTest extends BaseUnitTest {

    AutoCloseable openMocks;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(AvaliacaoController.class);

    @Mock
    private AvaliacaoService avaliacaoService;

    private ObjectMapper objectMapper;

    private AvaliacaoRequest request;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        request = new AvaliacaoRequest();
        request.setId(1L);
        request.setRestauranteId(10L);
        request.setUsuarioId(1L);
        request.setNota(5);
        request.setComentario("Ótima comida!");

        AvaliacaoController controller = new AvaliacaoController(avaliacaoService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class SalvarAvaliacaoControllerTest{
        @Test
        void testAvaliar() throws Exception {
            when(avaliacaoService.save(any(AvaliacaoDTO.class))).thenReturn(new AvaliacaoDTO(request));

            mockMvc.perform(post("/avaliacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.restauranteId").value(10L))
                    .andExpect(jsonPath("$.nota").value(5))
                    .andExpect(jsonPath("$.comentario").value("Ótima comida!"));

            verify(avaliacaoService, times(1)).save(any(AvaliacaoDTO.class));
            assertThat(logTracker.size()).isPositive();
            assertThat(logTracker.contains("requisição para buscar avaliação foi efetuada")).isTrue();
        }

        @Test
        void testAvaliarExcecaoQuandoJsonInvalido() throws Exception {
            AvaliacaoRequest req = getRandom(AvaliacaoRequest.class);
            req.setNota(5);
            req.setUsuarioId(1L);
            req.setDatapost(LocalDateTime.now());
            req.setRestauranteId(null);


            mockMvc.perform(post("/avaliacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nota\": 8, \"comentario\": \"Adorei! Atendimento excelente!\"," +
                                    " \"usuarioId\": 1}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.erro").value("Erro na validação de dados"))
                    .andExpect(jsonPath("$.detalhe").value("O restaurante não pode ser nulo. " +
                            "Por favor, forneça um valor para o restaurante."))
                    .andExpect(jsonPath("$.campo").value("restauranteId"))
                    .andExpect(jsonPath("$.statusCode").value(400));
            verify(avaliacaoService, never()).save(any(AvaliacaoDTO.class));
        }
    }

    @Nested
    class BuscarAvaliacaoControllerTest{
        @Test
        void testBuscarAvaliacaoPorRestaurante() throws Exception {
            List<AvaliacaoDTO> avaliacoes = Collections.singletonList(new AvaliacaoDTO(request));

            when(avaliacaoService.listarPorRestaurante(10L)).thenReturn(avaliacoes);

            mockMvc.perform(get("/avaliacao/10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].restauranteId").value(10L))
                    .andExpect(jsonPath("$[0].nota").value(5))
                    .andExpect(jsonPath("$[0].comentario").value("Ótima comida!"));

            verify(avaliacaoService, times(1)).listarPorRestaurante(10L);
            assertThat(logTracker.size()).isEqualTo(1);
            assertThat(logTracker.contains("requisição para buscar avaliação pelo idRestaurante foi efetuada"))
                    .isTrue();
        }

        @Test
        void testBuscarAvaliacaoPorRestauranteQuandoRestauranteNaoEncontrado() throws Exception {
            var id = "2";
            mockMvc.perform(get("/avaliacao/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            verify(avaliacaoService, times(1)).listarPorRestaurante(any());
        }
    }
}
