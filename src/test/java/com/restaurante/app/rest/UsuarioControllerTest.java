package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO(1L, "Fulano", "email@email.com", "11999999999");
    }

    @Test
    void cadastro_ReturnsUsuarioDTO() {
        when(usuarioService.save(usuarioDTO)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.cadastro(usuarioDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioDTO, response.getBody());
    }

    @Test
    void atualizar_ReturnsUpdatedUsuarioDTO() {
        when(usuarioService.update(1L, usuarioDTO)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.atualizar(1L, usuarioDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioDTO, response.getBody());
    }
}
