package com.restaurante.app.rest.controller;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.dto.MessageSuccessDTO;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<FuncionamentoDTO> cadastro(@Valid @RequestBody FuncionamentoRequest request) {
        log.info("requisição para cadastrar funcionamento foi efetuada");
        return ResponseEntity.ok(service.save(new FuncionamentoDTO(request)));
    }

    @Operation(summary = "Atualizar Funcionamento",
            description = "Atualizar o horário de Funcionamento do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @ApiResponse(responseCode = HttpStatusCodes.NOT_FOUND, description = "Registro não encontrado.")
    @PutMapping("/atualizar/{idFuncionamento}")
    public ResponseEntity<FuncionamentoDTO> atualizar(@PathVariable(name = "idFuncionamento") Long idFuncionamento,
                                                      @Valid @RequestBody FuncionamentoRequest request) {
        log.info("requisição para atualizar funcionamento foi efetuada");
        return ResponseEntity.ok(service.update(idFuncionamento, new FuncionamentoDTO(request)));
    }

    @Operation(summary = "Deletar registro de Funcionamento",
            description = "Deletar registro de horário de Funcionamento do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @DeleteMapping("/deletar/{idFuncionamento}")
    public ResponseEntity<MessageSuccessDTO> deletar(@PathVariable(name = "idFuncionamento") Long idFuncionamento) {
        service.delete(idFuncionamento);
        log.info("requisição para deletar funcionamento foi efetuada");
        return ResponseEntity.ok(MessageSuccessDTO.builder().mensagem("Registro removido com sucesso").build());
    }

    @Operation(summary = "Buscar horários de funcionamento do restaurante",
            description = "Buscar horários de funcionamento do restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<FuncionamentoDTO>> buscarPorRestaurante(
            @PathVariable(name = "idRestaurante") Long idRestaurante) {
        log.info("requisição para buscar funcionamento pelo idRestaurante foi efetuada");
        return ResponseEntity.ok(service.buscarPorRestaurante(idRestaurante));
    }

}