package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
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
@RequestMapping("/funcionamento")
@Slf4j
public class FuncionamentoController {

    @Autowired
    FuncionamentoService service;

    @Operation(summary = "Cadastrar Funcionamento",
            description = "Salvar Funcionamento.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<FuncionamentoDTO> cadastro(FuncionamentoEntity entity) {
        return ResponseEntity.ok(service.save(entity));
    }

}