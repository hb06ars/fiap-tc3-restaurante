package com.restaurante.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AvaliacaoControllerTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @Mock
    private AvaliacaoService avaliacaoService;

    private ObjectMapper objectMapper; // Instanciação do ObjectMapper

    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper(); // Instanciando o ObjectMapper
        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(1L);
        avaliacaoDTO.setRestauranteId(10L);
        avaliacaoDTO.setNota(5);
        avaliacaoDTO.setComentario("Ótima comida!");

        AvaliacaoController controller = new AvaliacaoController(avaliacaoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void testAvaliar() throws Exception {
        when(avaliacaoService.save(any(AvaliacaoDTO.class))).thenReturn(avaliacaoDTO);

        mockMvc.perform(post("/avaliacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacaoDTO))) // Usando o ObjectMapper para converter o DTO em String JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.restauranteId").value(10L))
                .andExpect(jsonPath("$.nota").value(5))
                .andExpect(jsonPath("$.comentario").value("Ótima comida!"));

        Mockito.verify(avaliacaoService, Mockito.times(1)).save(any(AvaliacaoDTO.class));
    }

    @Test
    void testBuscarAvaliacaoPorRestaurante() throws Exception {
        List<AvaliacaoDTO> avaliacoes = Collections.singletonList(avaliacaoDTO);

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

        Mockito.verify(avaliacaoService, Mockito.times(1)).listarPorRestaurante(10L);
    }
}
