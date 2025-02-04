package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvaliacaoControllerTest {

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

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

        verify(avaliacaoService, Mockito.times(1)).save(any(AvaliacaoDTO.class));
        assertThat(logTracker.size()).isEqualTo(1);
    }

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

        verify(avaliacaoService, Mockito.times(1)).listarPorRestaurante(10L);
        assertThat(logTracker.size()).isEqualTo(1);
    }
}
