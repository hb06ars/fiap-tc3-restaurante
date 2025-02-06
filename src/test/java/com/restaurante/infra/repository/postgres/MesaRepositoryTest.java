package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MesaRepositoryTest {

    @Mock
    private MesaRepository mesaRepository;

    private MesaEntity mesa;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mesa = new MesaEntity();
        mesa.setId(1L);
        mesa.setNomeMesa("Mesa 1");
        mesa.setRestauranteId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class CadastrarMesaRepositoryTest {
        @Test
        void testSalvarMesa() {
            when(mesaRepository.save(mesa)).thenReturn(mesa);

            MesaEntity savedMesa = mesaRepository.save(mesa);

            assertNotNull(savedMesa);
            assertEquals(1L, savedMesa.getId());
            assertEquals("Mesa 1", savedMesa.getNomeMesa());
            assertEquals(1L, savedMesa.getRestauranteId());

            verify(mesaRepository, times(1)).save(mesa);
        }
    }


    @Nested
    class BuscarMesaRepositoryTest {
        @Test
        void testBuscarPorId() {
            when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));

            Optional<MesaEntity> foundMesa = mesaRepository.findById(1L);

            assertTrue(foundMesa.isPresent());
            assertEquals(1L, foundMesa.get().getId());
            verify(mesaRepository, times(1)).findById(1L);
        }

        @Test
        void testBuscarMesasDisponiveis() {
            LocalDateTime dataReserva = LocalDateTime.of(2025, 2, 1, 19, 0);
            Object[] mesaDisponivel1 = {1L, "Mesa 1", "Disponível"};
            Object[] mesaDisponivel2 = {2L, "Mesa 2", "Disponível"};
            List<Object[]> mesasDisponiveis = Arrays.asList(mesaDisponivel1, mesaDisponivel2);

            when(mesaRepository.buscarMesasDisponiveis(1L, dataReserva)).thenReturn(mesasDisponiveis);

            List<Object[]> resultado = mesaRepository.buscarMesasDisponiveis(1L, dataReserva);

            assertFalse(resultado.isEmpty());
            assertEquals(2, resultado.size());
            assertEquals("Mesa 1", resultado.get(0)[1]);
            assertEquals("Disponível", resultado.get(0)[2]);
            assertEquals("Mesa 2", resultado.get(1)[1]);
            assertEquals("Disponível", resultado.get(1)[2]);
            verify(mesaRepository, times(1)).buscarMesasDisponiveis(1L, dataReserva);
        }

        @Test
        void testBuscarTodasPorRestauranteId() {
            MesaEntity mesa2 = new MesaEntity();
            mesa2.setId(2L);
            mesa2.setNomeMesa("Mesa 2");
            mesa2.setRestauranteId(1L);
            List<MesaEntity> mesas = Arrays.asList(mesa, mesa2);

            when(mesaRepository.findAllByRestauranteId(1L)).thenReturn(mesas);

            List<MesaEntity> resultado = mesaRepository.findAllByRestauranteId(1L);

            assertEquals(2, resultado.size());
            assertEquals("Mesa 1", resultado.get(0).getNomeMesa());
            assertEquals("Mesa 2", resultado.get(1).getNomeMesa());
            verify(mesaRepository, times(1)).findAllByRestauranteId(1L);
        }
    }

    @Nested
    class AtualizarMesaRepositoryTest {
        @Test
        void testAtualizarMesa() {
            MesaEntity mesaAtualizada = new MesaEntity();
            mesaAtualizada.setId(1L);
            mesaAtualizada.setNomeMesa("Mesa VIP");
            mesaAtualizada.setRestauranteId(1L);

            when(mesaRepository.save(mesaAtualizada)).thenReturn(mesaAtualizada);
            MesaEntity updatedMesa = mesaRepository.save(mesaAtualizada);

            assertEquals("Mesa VIP", updatedMesa.getNomeMesa());
            verify(mesaRepository, times(1)).save(mesaAtualizada);
        }
    }

    @Nested
    class DeletarMesaRepositoryTest {
        @Test
        void testDeletarMesa() {
            doNothing().when(mesaRepository).deleteById(1L);
            mesaRepository.deleteById(1L);
            verify(mesaRepository, times(1)).deleteById(1L);
        }
    }
}
