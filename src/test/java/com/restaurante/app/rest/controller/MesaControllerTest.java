package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
import com.restaurante.utils.BaseUnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MesaControllerTest extends BaseUnitTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(MesaController.class);


    @Mock
    private MesaService mesaService;

    private ObjectMapper objectMapper;

    private MesaDTO mesaDTO;
    private MesaDisponivelDTO mesaDisponivelDTO;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mesaDTO = new MesaDTO();
        mesaDTO.setId(1L);
        mesaDTO.setNomeMesa("Mesa 1");
        mesaDTO.setRestauranteId(10L);

        mesaDisponivelDTO = new MesaDisponivelDTO();
        mesaDisponivelDTO.setMesaId(1L);
        mesaDisponivelDTO.setMesaNome("Mesa 1");
        mesaDisponivelDTO.setStatusMesa("Disponível");

        MesaController controller = new MesaController(mesaService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void testCadastrarMesa() throws Exception {
        when(mesaService.save(any(MesaDTO.class))).thenReturn(mesaDTO);

        mockMvc.perform(post("/mesa/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeMesa").value("Mesa 1"))
                .andExpect(jsonPath("$.restauranteId").value(10L));

        Mockito.verify(mesaService, Mockito.times(1)).save(any(MesaDTO.class));
    }

    @Test
    void testCadastrarExcecaoQuandoJsonInvalido() throws Exception {
        var mesa = getRandom(MesaDTO.class);
        mesa.setRestauranteId(null);

        mockMvc.perform(post("/mesa/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesa)))
                .andExpect(status().isBadRequest());

        Mockito.verify(mesaService, Mockito.times(0)).save(any(MesaDTO.class));
    }

    @Test
    void testAtualizarMesa() throws Exception {
        when(mesaService.update(eq(1L), any(MesaDTO.class))).thenReturn(mesaDTO);

        mockMvc.perform(put("/mesa/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeMesa").value("Mesa 1"))
                .andExpect(jsonPath("$.restauranteId").value(10L));

        Mockito.verify(mesaService, Mockito.times(1)).update(eq(1L), any(MesaDTO.class));
    }

    @Test
    void testBuscarMesaPorId() throws Exception {
        List<MesaDisponivelDTO> mesas = Arrays.asList(mesaDisponivelDTO);

        when(mesaService.buscarMesas(1L)).thenReturn(mesas);

        mockMvc.perform(get("/mesa/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].mesaId").value(1L))
                .andExpect(jsonPath("$[0].mesaNome").value("Mesa 1"))
                .andExpect(jsonPath("$[0].statusMesa").value("Disponível"));

        Mockito.verify(mesaService, Mockito.times(1)).buscarMesas(1L);
    }

    @Test
    void testBuscarMesasPorRestaurante() throws Exception {
        List<MesaDTO> mesas = Arrays.asList(mesaDTO);

        when(mesaService.findAllByIdRestaurante(10L)).thenReturn(mesas);

        mockMvc.perform(get("/mesa/listaporrestaurante/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nomeMesa").value("Mesa 1"))
                .andExpect(jsonPath("$[0].restauranteId").value(10L));

        Mockito.verify(mesaService, Mockito.times(1)).findAllByIdRestaurante(10L);
    }
}
