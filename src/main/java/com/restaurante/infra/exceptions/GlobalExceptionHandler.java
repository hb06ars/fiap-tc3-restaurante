package com.restaurante.infra.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.restaurante.domain.dto.MessageErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("erro", "Erro de formatação no JSON");
        response.put("detalhe", "Erro ao tentar ler o JSON: " + ex.getMessage());
        response.put("statusCode", 400);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("erro", "Erro de formatação no JSON");
        response.put("detalhe", ex.getMessage());
        response.put("statusCode", 400);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        BindingResult result = ex.getBindingResult();

        for (FieldError error : result.getFieldErrors()) {
            response.put("erro", "Erro na validação de dados");
            response.put("detalhe", error.getDefaultMessage());
            response.put("campo", error.getField());
            response.put("statusCode", 400);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ListErrorResponse> handleValidationException(WebExchangeBindException ex) {
        List<MessageErrorDTO> errorMessages = ex.getFieldErrors().stream()
                .map(error ->
                        MessageErrorDTO.builder()
                                .detalhe(String.format("Campo: %s. %s", error.getField(), error.getDefaultMessage()))
                                .erro(error.getDefaultMessage())
                                .build())
                .collect(Collectors.toList());

        ListErrorResponse ListErrorResponse = new ListErrorResponse(
                errorMessages,
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(ListErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ListErrorResponse> handleObjectNotFoundException(ObjectNotFoundException e) {
        ListErrorResponse ListErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("O objeto solicitado não foi encontrado no sistema")
                        .build()),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(ListErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<ListErrorResponse> handleRecordAlreadyExistsException(RecordAlreadyExistsException e) {
        ListErrorResponse listErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("O registro já existente.")
                        .build()),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(listErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservaException.class)
    public ResponseEntity<ListErrorResponse> handleReservaException(ReservaException e) {
        ListErrorResponse listErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("Erro ao efetuar reserva")
                        .build()),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(listErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FieldNotFoundException.class)
    public ResponseEntity<ListErrorResponse> handleFieldNotFoundException(FieldNotFoundException e) {
        ListErrorResponse listErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("Campo faltando.")
                        .build()),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(listErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CapacidadeException.class)
    public ResponseEntity<ListErrorResponse> handleFieldNotFoundException(CapacidadeException e) {
        ListErrorResponse listErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("Capacidade Inválida.")
                        .build()),
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(listErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ListErrorResponse> handleErrorSaveObjectException(Exception e) {
        ListErrorResponse ListErrorResponse = new ListErrorResponse(
                List.of(MessageErrorDTO.builder()
                        .detalhe(e.getMessage())
                        .erro("Erro no sistema")
                        .build()),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(ListErrorResponse, HttpStatus.NOT_FOUND);
    }


}
