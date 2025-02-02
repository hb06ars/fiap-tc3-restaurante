package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UsuarioServiceIT  extends BaseUnitTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private UsuarioEntity usuarioEntity;

    @Test
    void save_ReturnsUsuarioDTO_WhenUserDoesNotExist() {
        UsuarioDTO result = usuarioService.save(usuarioDTO);
    }

    @Test
    void save_ThrowsRecordAlreadyExistsException_WhenEmailOrCelularExists() {
        RecordAlreadyExistsException thrown = assertThrows(RecordAlreadyExistsException.class, () -> usuarioService.save(usuarioDTO));
    }

    @Test
    void findAll_ReturnsListOfUsuarios() {
        List<UsuarioDTO> result = usuarioService.findAll();
    }

    @Test
    void findById_ReturnsUsuarioDTO() {
        UsuarioDTO result = usuarioService.findById(1L);
    }

    @Test
    void findById_ReturnsNull_WhenUsuarioNotFound() {
        UsuarioDTO result = usuarioService.findById(1L);
    }

    @Test
    void update_ReturnsUpdatedUsuarioDTO() {
        UsuarioDTO result = usuarioService.update(1L, null);
    }

    @Test
    void update_ThrowsRuntimeException_WhenUsuarioNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> usuarioService.update(1L, usuarioDTO));
    }

    @Test
    void update_ThrowsRuntimeException_WhenEmailOrCelularAlreadyExists() {
        assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
    }

    @Test
    void delete_Success() {
        usuarioService.delete(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenUsuarioNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> usuarioService.delete(1L));
    }
}
