package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FuncionamentoServiceTest {

    AutoCloseable openMocks;

    @InjectMocks
    private FuncionamentoService funcionamentoService;

    @Mock
    private FuncionamentoRepository funcionamentoRepository;

    private FuncionamentoDTO funcionamentoDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks = MockitoAnnotations.openMocks(this);

        Field toleranciaMesaField = FuncionamentoService.class.getDeclaredField("toleranciaMesa");
        toleranciaMesaField.setAccessible(true);
        toleranciaMesaField.set(funcionamentoService, 1);


        funcionamentoDTO = new FuncionamentoDTO();
        funcionamentoDTO.setId(1L);
        funcionamentoDTO.setAbertura(LocalTime.parse("08:00:00"));
        funcionamentoDTO.setFechamento(LocalTime.parse("18:00:00"));
        funcionamentoDTO.setDiaEnum(DiaEnum.SEGUNDA);
        funcionamentoDTO.setRestauranteId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testSave() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.save(any(FuncionamentoEntity.class))).thenReturn(funcionamentoEntity);
        FuncionamentoDTO savedFuncionamento = funcionamentoService.save(funcionamentoDTO);

        assertNotNull(savedFuncionamento);
        assertEquals(funcionamentoDTO.getId(), savedFuncionamento.getId());
        verify(funcionamentoRepository, times(1)).save(any(FuncionamentoEntity.class));
    }

    @Test
    void testFindAll() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.findAll()).thenReturn(List.of(funcionamentoEntity));
        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.findAll();
        assertNotNull(funcionamentoList);
        assertEquals(1, funcionamentoList.size());
        verify(funcionamentoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.of(funcionamentoEntity));
        FuncionamentoDTO foundFuncionamento = funcionamentoService.findById(1L);
        assertNotNull(foundFuncionamento);
        assertEquals(funcionamentoDTO.getId(), foundFuncionamento.getId());
        verify(funcionamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.empty());
        FuncionamentoDTO foundFuncionamento = funcionamentoService.findById(1L);
        assertNull(foundFuncionamento);
        verify(funcionamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate_Success() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.of(funcionamentoEntity));
        when(funcionamentoRepository.save(any(FuncionamentoEntity.class))).thenReturn(funcionamentoEntity);

        FuncionamentoDTO updatedFuncionamento = funcionamentoService.update(1L, funcionamentoDTO);

        assertNotNull(updatedFuncionamento);
        assertEquals(funcionamentoDTO.getId(), updatedFuncionamento.getId());
        verify(funcionamentoRepository, times(1)).findById(1L);
        verify(funcionamentoRepository, times(1)).save(any(FuncionamentoEntity.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> funcionamentoService.update(1L, funcionamentoDTO));
        verify(funcionamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete_Success() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.of(funcionamentoEntity));
        funcionamentoService.delete(1L);

        verify(funcionamentoRepository, times(1)).findById(1L);
        verify(funcionamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> funcionamentoService.delete(1L));
        verify(funcionamentoRepository, times(1)).findById(1L);
    }

    @Test
    void testValidarDataFuncionamento() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.validarData(any(), any(), any())).thenReturn(List.of(funcionamentoEntity));

        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.validarDataFuncionamento(1L, LocalDateTime.now(), DiaEnum.SEGUNDA);

        assertNotNull(funcionamentoList);
        assertEquals(1, funcionamentoList.size());
        verify(funcionamentoRepository, times(1)).validarData(any(), any(), any());
    }

    @Test
    void testBuscarPorRestaurante() {
        FuncionamentoEntity funcionamentoEntity = new FuncionamentoEntity(funcionamentoDTO);
        when(funcionamentoRepository.findAllByRestauranteId(1L)).thenReturn(List.of(funcionamentoEntity));

        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.buscarPorRestaurante(1L);

        assertNotNull(funcionamentoList);
        assertEquals(1, funcionamentoList.size());
        verify(funcionamentoRepository, times(1)).findAllByRestauranteId(1L);
    }

    @Test
    void testInserirDataFuncionamentoInicial() {
        when(funcionamentoRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        funcionamentoService.inserirDataFuncionamentoInicial(1L);
        verify(funcionamentoRepository, times(1)).saveAll(anyList());
    }
}
