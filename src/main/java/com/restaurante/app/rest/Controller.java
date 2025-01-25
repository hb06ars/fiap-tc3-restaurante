package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurante")
@Slf4j
public class Controller {

    @Autowired
    RestauranteService restauranteService;

    @Operation(summary = "Cadastrar Restaurante",
            description = "Cadastro realizado pelo restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<RestauranteDTO> cadastro(RestauranteEntity entity) {
        return ResponseEntity.ok(restauranteService.save(entity));
    }

}