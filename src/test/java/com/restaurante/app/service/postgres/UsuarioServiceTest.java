package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    AutoCloseable openMocks;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        usuarioDTO = new UsuarioDTO(1L, "Usuário A", "usuarioA@email.com", "123456789");
        usuarioEntity = new UsuarioEntity(usuarioDTO);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void save_ReturnsUsuarioDTO_WhenUserDoesNotExist() {
        when(usuarioRepository.findByEmailOrCelular(anyString(), anyString())).thenReturn(null);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioDTO result = usuarioService.save(usuarioDTO);

        assertNotNull(result);
        assertEquals(usuarioDTO.getEmail(), result.getEmail());
        assertEquals(usuarioDTO.getCelular(), result.getCelular());
    }

    @Test
    void save_ThrowsRecordAlreadyExistsException_WhenEmailOrCelularExists() {
        when(usuarioRepository.findByEmailOrCelular(anyString(), anyString())).thenReturn(usuarioEntity);

        RecordAlreadyExistsException thrown = assertThrows(RecordAlreadyExistsException.class, () -> usuarioService.save(usuarioDTO));

        assertEquals("O email ou celular já existem no sistema.", thrown.getMessage());
    }

    @Test
    void findAll_ReturnsListOfUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity));

        List<UsuarioDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuarioDTO.getNome(), result.get(0).getNome());
    }

    @Test
    void findById_ReturnsUsuarioDTO() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));

        UsuarioDTO result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(usuarioDTO.getNome(), result.getNome());
    }

    @Test
    void findById_ReturnsNull_WhenUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioDTO result = usuarioService.findById(1L);

        assertNull(result);
    }

    @Test
    void update_ReturnsUpdatedUsuarioDTO() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioDTO updatedDTO = new UsuarioDTO(1L, "Usuário A", "usuarioA@email.com", "987654321");
        UsuarioDTO result = usuarioService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.getNome(), result.getNome());
        assertEquals(updatedDTO.getCelular(), result.getCelular());
    }

    @Test
    void update_ThrowsRuntimeException_WhenUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> usuarioService.update(1L, usuarioDTO));

        assertEquals("Usuário 1 não encontrado.", thrown.getMessage());
    }

    @Test
    void update_ThrowsRuntimeException_WhenEmailOrCelularAlreadyExists() {
        UsuarioDTO updatedDTO = new UsuarioDTO(1L, "Usuário A", "usuarioA@email.com", "987654321");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.findByEmailOrCelular(anyString(), anyString())).thenReturn(new UsuarioEntity(updatedDTO));

        assertThrows(RuntimeException.class, () -> usuarioService.update(1L, updatedDTO));

    }

    @Test
    void delete_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));
        usuarioService.delete(1L);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> usuarioService.delete(1L));

        assertEquals("Usuário com ID: 1, não encontrado.", thrown.getMessage());
    }
}
