package com.restaurante.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Teste");

        when(usuarioService.save(usuarioDTO)).thenReturn(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Usuário Teste"));

        verify(usuarioService, times(1)).save(usuarioDTO);
    }

    @Test
    void atualizar_DeveRetornarUsuarioDTOAtualizado() throws Exception {
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Atualizado");

        when(usuarioService.update(id, usuarioDTO)).thenReturn(usuarioDTO);

        mockMvc.perform(put("/usuario/atualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Usuário Atualizado"));

        verify(usuarioService, times(1)).update(id, usuarioDTO);
    }
}
