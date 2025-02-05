package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurante.app.rest.request.RestauranteRequest;
import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestauranteControllerTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(RestauranteController.class);

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        RestauranteController controller = new RestauranteController(restauranteService, cadastrarRestauranteUseCase,
                atualizarRestauranteUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void cadastrar_DeveRetornarRestauranteDTO() throws Exception {
        RestauranteRequest request = new RestauranteRequest();
        request.setNome("Restaurante Teste");
        request.setLocalizacao("São Paulo");
        request.setCapacidade(10);
        request.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        RestauranteDTO dto = new RestauranteDTO(request);

        when(cadastrarRestauranteUseCase.execute(dto)).thenReturn(dto);

        mockMvc.perform(post("/restaurante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));

        verify(cadastrarRestauranteUseCase, times(1)).execute(dto);
    }

    @Test
    void atualizar_DeveRetornarRestauranteDTOAtualizado() throws Exception {
        Long id = 1L;
        RestauranteRequest request = new RestauranteRequest();
        request.setNome("Restaurante Atualizado");
        request.setLocalizacao("São Paulo");
        request.setCapacidade(10);
        request.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        var dto = new RestauranteDTO(request);

        when(atualizarRestauranteUseCase.execute(id, dto)).thenReturn(dto);

        mockMvc.perform(put("/restaurante/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Restaurante Atualizado"));

        verify(atualizarRestauranteUseCase, times(1)).execute(id, dto);
    }

    @Test
    void buscar_DeveRetornarListaDeRestaurantes() throws Exception {
        RestauranteDTO restaurante1 = new RestauranteDTO();
        restaurante1.setNome("Restaurante 1");
        RestauranteDTO restaurante2 = new RestauranteDTO();
        restaurante2.setNome("Restaurante 2");

        when(restauranteService.buscarRestaurantes("Restaurante 1", "São Paulo",
                TipoCozinhaEnum.BRASILEIRA.name())).thenReturn(List.of(restaurante1, restaurante2));

        mockMvc.perform(get("/restaurante")
                        .param("nome", "Restaurante 1")
                        .param("localizacao", "São Paulo")
                        .param("tipoCozinha", TipoCozinhaEnum.BRASILEIRA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Restaurante 1"))
                .andExpect(jsonPath("$[1].nome").value("Restaurante 2"));

        verify(restauranteService, times(1)).buscarRestaurantes("Restaurante 1",
                "São Paulo", TipoCozinhaEnum.BRASILEIRA.name());
    }

    @Test
    void deletar_DeveRetornarMensagemDeSucesso() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/restaurante/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Registro deletado com sucesso."));

        verify(restauranteService, times(1)).delete(id);
    }
}
