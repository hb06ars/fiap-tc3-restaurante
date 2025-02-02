package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.UsuarioEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testBuscarPorEmailOuCelular() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
        
        var usuarioSaved = usuarioRepository.save(usuarioEntity);
        var result = usuarioRepository.findByEmailOrCelular(usuarioSaved.getEmail(), usuarioSaved.getCelular());

        assertNotNull(result);
        assertThat(result.getId()).isEqualTo(usuarioSaved.getId());

    }

    @Test
    void testSalvarUsuario() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);

        var usuarioSaved = usuarioRepository.save(usuarioEntity);

        assertNotNull(usuarioSaved);
        assertThat(usuarioSaved.getId()).isPositive();
    }

    @Test
    void testBuscarPorId() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
        var usuarioSaved = usuarioRepository.save(usuarioEntity);

        var result = usuarioRepository.findById(usuarioSaved.getId());

        assertNotNull(result);
        assertThat(result.get().getId()).isEqualTo(usuarioSaved.getId());
    }

    @Test
    void testAtualizarUsuario() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
        usuarioEntity.setNome("Fulano");

        var usuarioSaved = usuarioRepository.save(usuarioEntity);
        usuarioSaved.setNome("Fulano de Tal");
        var result = usuarioRepository.save(usuarioEntity);

        assertNotNull(result);
        assertThat(result.getNome()).isEqualTo("Fulano de Tal");
    }

    @Test
    void testDeletarUsuario() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
        var usuarioSaved = usuarioRepository.save(usuarioEntity);

        usuarioRepository.deleteById(usuarioSaved.getId());
        var result = usuarioRepository.findById(usuarioSaved.getId());

        assertFalse(result.isPresent());
    }
}
