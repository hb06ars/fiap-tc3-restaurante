package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.enums.StatusReservaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MesaControllerTest {

    @Mock
    private MesaService mesaService;

    @InjectMocks
    private MesaController mesaController;

    private MesaDTO mesaDTO;
    private List<MesaDisponivelDTO> mesaDisponivelDTOList;
    private List<MesaDTO> mesaDTOList;

    @BeforeEach
    void setUp() {
        mesaDTO = new MesaDTO(1L, "Mesa 1", 1L);
        mesaDisponivelDTOList = List.of(new MesaDisponivelDTO(1L, "Mesa 1", StatusReservaEnum.OCUPADO.name()));
        mesaDTOList = List.of(mesaDTO);
    }

    @Test
    void cadastro_ReturnsMesaDTO() {
        when(mesaService.save(mesaDTO)).thenReturn(mesaDTO);

        ResponseEntity<MesaDTO> response = mesaController.cadastro(mesaDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mesaDTO, response.getBody());
    }

    @Test
    void atualizar_ReturnsUpdatedMesaDTO() {
        when(mesaService.update(1L, mesaDTO)).thenReturn(mesaDTO);

        ResponseEntity<MesaDTO> response = mesaController.atualizar(1L, mesaDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mesaDTO, response.getBody());
    }

    @Test
    void buscarMesas_ReturnsMesaDisponivelDTOList() {
        when(mesaService.buscarMesas(1L)).thenReturn(mesaDisponivelDTOList);

        ResponseEntity<List<MesaDisponivelDTO>> response = mesaController.buscarMesas(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mesaDisponivelDTOList, response.getBody());
    }

    @Test
    void buscarMesasPorRestaurante_ReturnsMesaDTOList() {
        when(mesaService.findAllByIdRestaurante(1L)).thenReturn(mesaDTOList);

        ResponseEntity<List<MesaDTO>> response = mesaController.buscarMesasPorRestaurante(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mesaDTOList, response.getBody());
    }
}
