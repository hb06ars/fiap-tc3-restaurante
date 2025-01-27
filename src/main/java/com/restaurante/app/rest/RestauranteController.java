package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    RestauranteService service;

    @Autowired
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @Autowired
    AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Operation(summary = "Cadastrar Restaurante",
            description = "Cadastrr Restaurante no sistema.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrar(@Valid @RequestBody RestauranteEntity entity) {
        return ResponseEntity.ok(cadastrarRestauranteUseCase.execute(entity));
    }

    @Operation(summary = "Atualizar Restaurante",
            description = "Atualização do restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody RestauranteEntity entity) {
        return ResponseEntity.ok(atualizarRestauranteUseCase.execute(id, entity));
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

}