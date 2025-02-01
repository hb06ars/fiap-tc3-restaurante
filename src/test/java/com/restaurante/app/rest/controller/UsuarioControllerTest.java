package com.restaurante.app.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurante.app.rest.request.UsuarioRequest;
import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.UsuarioDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void cadastro_DeveRetornarUsuarioDTO() throws Exception {
        UsuarioRequest request = new UsuarioRequest();
        request.setCelular("11888888888");
        request.setEmail("email@email.com");
        request.setNome("Usuário");
        var dto = new UsuarioDTO(request);

        when(usuarioService.save(dto)).thenReturn(dto);

        mockMvc.perform(post("/usuario/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Usuário"));

        verify(usuarioService, times(1)).save(dto);
    }

    @Test
    void atualizar_DeveRetornarUsuarioDTOAtualizado() throws Exception {
        Long id = 1L;
        UsuarioRequest request = new UsuarioRequest();
        request.setCelular("11888888888");
        request.setEmail("email@email.com");
        request.setNome("Usuário Atualizado");
        var dto = new UsuarioDTO(request);

        when(usuarioService.update(id, dto)).thenReturn(dto);

        mockMvc.perform(put("/usuario/atualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Usuário Atualizado"));

        verify(usuarioService, times(1)).update(id, dto);
    }
}
