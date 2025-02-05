package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.infra.exceptions.CapacidadeException;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MesaServiceTest {

    AutoCloseable openMocks;

    @InjectMocks
    private MesaService mesaService;

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    private MesaDTO mesaDTO;
    private MesaEntity mesaEntity;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        mesaDTO = new MesaDTO();
        mesaDTO.setRestauranteId(1L);
        mesaDTO.setNomeMesa("Mesa 1");

        mesaEntity = new MesaEntity();
        mesaEntity.setRestauranteId(1L);
        mesaEntity.setNomeMesa("Mesa 1");
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void save_ValidMesa_ReturnsMesaDTO() {
        var restaurante = RestauranteEntity.builder()
                .id(1L)
                .capacidade(100)
                .nome("Restaurante")
                .localizacao("Av. Paulista, 1000")
                .tipoCozinha(TipoCozinhaEnum.BRASILEIRA)
                .build();

        var mesa = MesaEntity.builder()
                .id(1L)
                .nomeMesa("Mesa 1")
                .restauranteId(1L)
                .build();

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(mesaRepository.findAllByRestauranteId(1L)).thenReturn(List.of(mesa));
        when(mesaRepository.save(any(MesaEntity.class))).thenReturn(mesaEntity);

        MesaDTO savedMesaDTO = mesaService.save(mesaDTO);

        assertNotNull(savedMesaDTO);
        assertEquals("Mesa 1", savedMesaDTO.getNomeMesa());
        verify(mesaRepository, times(1)).save(any(MesaEntity.class));
    }

    @Test
    void save_CapacidadeExcedida_ThrowsCapacidadeException() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(new RestauranteEntity()));
        when(mesaRepository.findAllByRestauranteId(1L))
                .thenReturn(Collections.singletonList(new MesaEntity()));

        assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
    }

    @Test
    void save_RestauranteNaoEncontrado_ThrowsObjectNotFoundException() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> mesaService.save(mesaDTO));
    }

    @Test
    void findAll_ReturnsMesaDTOList() {
        when(mesaRepository.findAll()).thenReturn(Collections.singletonList(mesaEntity));

        List<MesaDTO> mesas = mesaService.findAll();

        assertNotNull(mesas);
        assertEquals(1, mesas.size());
        assertEquals("Mesa 1", mesas.get(0).getNomeMesa());
    }

    @Test
    void findById_ExistingMesa_ReturnsMesaDTO() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));

        MesaDTO foundMesa = mesaService.findById(1L);

        assertNotNull(foundMesa);
        assertEquals("Mesa 1", foundMesa.getNomeMesa());
    }

    @Test
    void findById_MesaNaoEncontrada_ReturnsNull() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.empty());

        MesaDTO foundMesa = mesaService.findById(1L);

        assertNull(foundMesa);
    }

    @Test
    void update_ExistingMesa_ReturnsUpdatedMesaDTO() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));
        when(mesaRepository.save(any(MesaEntity.class))).thenReturn(mesaEntity);

        mesaDTO.setNomeMesa("Mesa Atualizada");
        MesaDTO updatedMesaDTO = mesaService.update(1L, mesaDTO);

        assertNotNull(updatedMesaDTO);
        assertEquals("Mesa Atualizada", updatedMesaDTO.getNomeMesa());
    }

    @Test
    void update_MesaNaoEncontrada_ThrowsRuntimeException() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mesaService.update(1L, mesaDTO));
    }

    @Test
    void delete_ExistingMesa_DeletesMesa() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesaEntity));

        mesaService.delete(1L);

        verify(mesaRepository, times(1)).deleteById(1L);
    }

    @Test
    void buscarMesas_ReturnsMesaDisponivelDTOList() {
        Object[] mesaArray = new Object[]{1L, "Mesa 1", StatusReservaEnum.OCUPADO.name()};
        when(mesaRepository.buscarMesasDisponiveis(any(), any())).thenReturn(List.<Object[]>of(mesaArray));
        List<MesaDisponivelDTO> mesasDisponiveis = mesaService.buscarMesas(1L);
        assertNotNull(mesasDisponiveis);
    }

    @Test
    void salvarTodasMesas_SavesAllMesas() {
        List<MesaEntity> mesas = Collections.singletonList(mesaEntity);

        when(mesaRepository.saveAll(anyList())).thenReturn(mesas);

        List<MesaDTO> savedMesas = mesaService.salvarTodasMesas(mesas);

        assertNotNull(savedMesas);
        assertEquals(1, savedMesas.size());
    }

    @Test
    void findAllByIdRestaurante_ReturnsMesaDTOList() {
        when(mesaRepository.findAllByRestauranteId(1L))
                .thenReturn(Collections.singletonList(mesaEntity));

        List<MesaDTO> mesas = mesaService.findAllByIdRestaurante(1L);

        assertNotNull(mesas);
        assertEquals(1, mesas.size());
        assertEquals("Mesa 1", mesas.get(0).getNomeMesa());
    }

    @Test
    void findAllByRestaurante_ReturnsListOfMesas_WhenRestauranteHasMesas() {
        List<MesaEntity> mesas = List.of(
                new MesaEntity(1L, "Mesa 1", 1L),
                new MesaEntity(2L, "Mesa 2", 1L)
        );

        Mockito.when(mesaRepository.findAllByRestauranteId(any())).thenReturn(mesas);

        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(1L);
        assertNotNull(mesasDTO);
        assertEquals(2, mesasDTO.size());
        assertEquals("Mesa 1", mesasDTO.get(0).getNomeMesa());
        assertEquals("Mesa 2", mesasDTO.get(1).getNomeMesa());
    }

    @Test
    void findAllByRestaurante_ReturnsEmptyList_WhenRestauranteHasNoMesas() {
        Mockito.when(mesaRepository.findAllByRestauranteId(any())).thenReturn(List.of());
        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(1L);

        assertNotNull(mesasDTO);
        assertTrue(mesasDTO.isEmpty());
    }

    @Test
    void save_MesasIsEmpty_ThrowsCapacidadeException() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(new RestauranteEntity()));
        when(mesaRepository.findAllByRestauranteId(1L)).thenReturn(List.of());
        assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
    }
}
