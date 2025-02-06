package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurante.app.rest.request.UsuarioRequest;
import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(UsuarioController.class);

    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        UsuarioController controller = new UsuarioController(usuarioService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastroUsuarioControllerTest{
        @Test
        void cadastroDeveRetornarUsuarioDTO() throws Exception {
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
    }

    @Nested
    class AtualizacaoUsuarioControllerTest{
        @Test
        void atualizarDeveRetornarUsuarioDTOAtualizado() throws Exception {
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

    @Nested
    class BuscaUsuarioControllerTest{
        @Test
        void deveRetornarUsuario() throws Exception {
            UsuarioRequest request = new UsuarioRequest();
            request.setCelular("11988887777");
            request.setEmail("email@email.com");
            request.setNome("Usuário");
            var dto = new UsuarioDTO(request);

            when(usuarioService.save(dto)).thenReturn(dto);

            mockMvc.perform(get("/usuario?celular=11988887777")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(usuarioService, times(1)).findByEmailOrCelular(any(), any());
        }
    }


}
