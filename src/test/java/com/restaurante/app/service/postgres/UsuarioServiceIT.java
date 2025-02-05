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
    void saveReturnsUsuarioDTOWhenUserDoesNotExist() {
        var result = usuarioService.save(getRandom(UsuarioDTO.class));

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void saveThrowsRecordAlreadyExistsExceptionWhenEmailOrCelularExists() {
        var usuarioSaved = usuarioService.save(getRandom(UsuarioDTO.class));
        assertThrows(RecordAlreadyExistsException.class, () -> usuarioService.save(usuarioSaved));
    }

    @Test
    void findAllReturnsListOfUsuarios() {
        usuarioService.save(getRandom(UsuarioDTO.class));
        List<UsuarioDTO> result = usuarioService.findAll();

        assertNotNull(result);
        assertThat(result.size()).isPositive();

    }

    @Test
    void findByIdReturnsUsuarioDTO() {
        var user = usuarioService.save(getRandom(UsuarioDTO.class));
        UsuarioDTO result = usuarioService.findById(user.getId());

        assertNotNull(result);
        assertThat(result.getId()).isPositive();

    }

    @Test
    void findByEmailOrCelularReturnsUsuarioDTO() {
        var user = usuarioService.save(getRandom(UsuarioDTO.class));
        UsuarioDTO result = usuarioService.findByEmailOrCelular(user.getEmail(), user.getCelular());

        assertNotNull(result);
        assertThat(result.getId()).isPositive();

    }

    @Test
    void findByEmailOrCelularReturnsNullwhenCelularIsNull() {
        assertThrows(RuntimeException.class, () -> usuarioService
                .findByEmailOrCelular("email@mail.com", null));
    }

    @Test
    void findByEmailOrCelularReturnsNullwhenEmailIsNull() {
        assertThrows(RuntimeException.class, () -> usuarioService
                .findByEmailOrCelular(null, "123456789"));
    }


    @Test
    void findByIdReturnsNullWhenUsuarioNotFound() {
        UsuarioDTO result = usuarioService.findById(1L);
        assertNull(result);
    }

    @Test
    void updateReturnsUpdatedUsuarioDTO() {
        var dto = getRandom(UsuarioDTO.class);
        dto.setNome("Nome inicial");
        var saved = usuarioService.save(dto);

        saved.setNome("Teste nome original");
        UsuarioDTO result = usuarioService.update(saved.getId(), saved);

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void updateThrowsRuntimeExceptionWhenUsuarioNotFound() {
        assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
    }

    @Test
    void updateThrowsRuntimeExceptionWhenEmailOrCelularAlreadyExists() {
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
    void deleteThrowsRuntimeExceptionWhenUsuarioNotFound() {
        assertThrows(RuntimeException.class, () -> usuarioService.delete(1L));
    }
}
