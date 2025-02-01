package com.restaurante.app.rest.controller;

import com.restaurante.app.rest.request.RestauranteRequest;
import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.MessageSuccessDTO;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurante")
@Slf4j
public class RestauranteController {

    private final RestauranteService service;
    private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;
    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    public RestauranteController(RestauranteService service, CadastrarRestauranteUseCase cadastrarRestauranteUseCase, AtualizarRestauranteUseCase atualizarRestauranteUseCase) {
        this.service = service;
        this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
        this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
    }

    @Operation(summary = "Cadastrar Restaurante",
            description = "Cadastrar Restaurante no sistema.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrar(@Valid @RequestBody RestauranteRequest request) {
        return ResponseEntity.ok(cadastrarRestauranteUseCase.execute(new RestauranteDTO(request)));
    }

    @Operation(summary = "Atualizar Restaurante",
            description = "Atualização do restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> atualizar(@PathVariable(name = "id") Long id,
                                                    @Valid @RequestBody RestauranteRequest request) {
        return ResponseEntity.ok(atualizarRestauranteUseCase.execute(id, new RestauranteDTO(request)));
    }

    @Operation(summary = "Buscar Restaurante",
            description = "Busca pelo restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> buscar(
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "") String localizacao,
            @RequestParam(defaultValue = "") String tipoCozinha) {
        return ResponseEntity.ok(service.buscarRestaurantes(nome, localizacao, tipoCozinha));
    }

    @Operation(summary = "Deletar Restaurante",
            description = "Deleção do restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Deleção realizada com sucesso.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageSuccessDTO> deletar(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return ResponseEntity.ok(MessageSuccessDTO.builder().mensagem("Registro deletado com sucesso.").build());
    }


}