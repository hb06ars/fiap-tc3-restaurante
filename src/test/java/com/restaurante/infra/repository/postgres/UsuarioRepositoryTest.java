package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.UsuarioEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioEntity usuario;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setCelular("11999999999");
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class BuscarUsuarioRespositoryTest {
        @Test
        void testBuscarPorEmailOuCelular() {
            when(usuarioRepository.findByEmailOrCelular("joao@email.com",
                    "11999999999")).thenReturn(usuario);

            UsuarioEntity foundUsuario = usuarioRepository.findByEmailOrCelular("joao@email.com", "11999999999");

            assertNotNull(foundUsuario);
            assertEquals("João Silva", foundUsuario.getNome());
            assertEquals("joao@email.com", foundUsuario.getEmail());

            verify(usuarioRepository, times(1)).findByEmailOrCelular("joao@email.com",
                    "11999999999");
        }

        @Test
        void testBuscarPorId() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            Optional<UsuarioEntity> foundUsuario = usuarioRepository.findById(1L);

            assertTrue(foundUsuario.isPresent());
            assertEquals(1L, foundUsuario.get().getId());

            verify(usuarioRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class SalvarUsuarioRepositoryTest {
        @Test
        void testSalvarUsuario() {
            when(usuarioRepository.save(usuario)).thenReturn(usuario);

            UsuarioEntity savedUsuario = usuarioRepository.save(usuario);

            assertNotNull(savedUsuario);
            assertEquals(1L, savedUsuario.getId());
            assertEquals("João Silva", savedUsuario.getNome());
            assertEquals("joao@email.com", savedUsuario.getEmail());

            verify(usuarioRepository, times(1)).save(usuario);
        }
    }

    @Nested
    class AtualizarUsuarioRepositoryTest {
        @Test
        void testAtualizarUsuario() {
            UsuarioEntity usuarioAtualizado = new UsuarioEntity();
            usuarioAtualizado.setId(1L);
            usuarioAtualizado.setNome("João Pedro");
            usuarioAtualizado.setEmail("joaopedro@email.com");
            usuarioAtualizado.setCelular("11988888888");

            when(usuarioRepository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado);

            UsuarioEntity updatedUsuario = usuarioRepository.save(usuarioAtualizado);

            assertEquals("João Pedro", updatedUsuario.getNome());
            assertEquals("joaopedro@email.com", updatedUsuario.getEmail());

            verify(usuarioRepository, times(1)).save(usuarioAtualizado);
        }
    }


    @Nested
    class DeletarUsuarioRepositoryTest {
        @Test
        void testDeletarUsuario() {
            doNothing().when(usuarioRepository).deleteById(1L);
            usuarioRepository.deleteById(1L);
            verify(usuarioRepository, times(1)).deleteById(1L);
        }
    }
}
