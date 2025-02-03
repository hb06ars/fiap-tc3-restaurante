package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UsuarioServiceIT extends BaseUnitTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void save_ReturnsUsuarioDTO_WhenUserDoesNotExist() {
        var result = usuarioService.save(getRandom(UsuarioDTO.class));

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void save_ThrowsRecordAlreadyExistsException_WhenEmailOrCelularExists() {
        var usuarioSaved = usuarioService.save(getRandom(UsuarioDTO.class));
        assertThrows(RecordAlreadyExistsException.class, () -> usuarioService.save(usuarioSaved));
    }

    @Test
    void findAll_ReturnsListOfUsuarios() {
        usuarioService.save(getRandom(UsuarioDTO.class));
        List<UsuarioDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertThat(result.size()).isPositive();

    }

    @Test
    void findById_ReturnsUsuarioDTO() {
        var user = usuarioService.save(getRandom(UsuarioDTO.class));
        UsuarioDTO result = usuarioService.findById(user.getId());

        assertNotNull(result);
        assertThat(result.getId()).isPositive();

    }

    @Test
    void findById_ReturnsNull_WhenUsuarioNotFound() {
        UsuarioDTO result = usuarioService.findById(1L);
    }

    @Test
    void update_ReturnsUpdatedUsuarioDTO() {
        var dto = getRandom(UsuarioDTO.class);
        dto.setNome("Nome inicial");
        var saved = usuarioService.save(dto);

        saved.setNome("Teste nome original");
        UsuarioDTO result = usuarioService.update(saved.getId(), saved);

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void update_ThrowsRuntimeException_WhenUsuarioNotFound() {
        assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
    }

    @Test
    void update_ThrowsRuntimeException_WhenEmailOrCelularAlreadyExists() {
        assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
    }

    @Test
    void deleteSuccess() {
        var saved = usuarioService.save(getRandom(UsuarioDTO.class));
        usuarioService.delete(saved.getId());
        var result = usuarioService.findById(saved.getId());
        assertNull(result);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenUsuarioNotFound() {
        assertThrows(RuntimeException.class, () -> usuarioService.delete(1L));
    }
}
