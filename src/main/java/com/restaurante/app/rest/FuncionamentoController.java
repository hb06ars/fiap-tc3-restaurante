package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.dto.MessageSuccessDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/funcionamento")
@Slf4j
public class FuncionamentoController {

    private final FuncionamentoService service;

    public FuncionamentoController(FuncionamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Funcionamento",
            description = "Cadastrar o horário de Funcionamento do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<FuncionamentoDTO> cadastro(FuncionamentoDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Atualizar Funcionamento",
            description = "Atualizar o horário de Funcionamento do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @PutMapping("/atualizar/{idFuncionamento}")
    public ResponseEntity<FuncionamentoDTO> atualizar(@PathVariable Long idFuncionamento,
                                                      FuncionamentoDTO dto) {
        return ResponseEntity.ok(service.update(idFuncionamento, dto));
    }

    @Operation(summary = "Deletar registro de Funcionamento",
            description = "Deletar registro de horário de Funcionamento do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @DeleteMapping("/deletar/{idFuncionamento}")
    public ResponseEntity<MessageSuccessDTO> deletar(@PathVariable Long idFuncionamento) {
        service.delete(idFuncionamento);
        return ResponseEntity.ok(MessageSuccessDTO.builder().mensagem("Registro removido com sucesso").build());
    }

    @Operation(summary = "Buscar horários de funcionamento do restaurante",
            description = "Buscar horários de funcionamento do restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<FuncionamentoDTO>> buscarPorRestaurante(@PathVariable Long idRestaurante) {
        return ResponseEntity.ok(service.buscarPorRestaurante(idRestaurante));
    }

}