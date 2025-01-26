package com.restaurante.app.rest;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/reserva")
@Slf4j
public class ReservaController {

    @Autowired
    ReservarMesaUseCase reservarMesaUseCase;

    @Operation(summary = "Cadastrar Reserva",
            description = "Salvar a Reserva.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping
    public ResponseEntity<ReservaDTO> cadastro(@Valid @RequestBody ReservaEntity entity) {
        return ResponseEntity.ok(reservarMesaUseCase.salvar(entity));
    }

    @Operation(summary = "Atualizar Reserva",
            description = "Atualizar a Reserva.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualizção realizada com sucesso.")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> atualizacao(@PathVariable Long id, @Valid @RequestBody ReservaEntity entity) {
        return ResponseEntity.ok(reservarMesaUseCase.atualizar(id, entity));
    }

}