package com.restaurante.app.rest;

import com.restaurante.domain.dto.MessageSuccessDTO;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurante")
@Slf4j
public class Controller {

    @Operation(summary = "Controller de Teste",
            description = "Está sendo criado um controller de Teste")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Controller executado com sucesso.")
    @GetMapping
    public ResponseEntity<MessageSuccessDTO> tarifa() {
        return ResponseEntity.ok(MessageSuccessDTO.builder().mensagem("Sucesso.").build());
    }

}