package com.restaurante.infra.exceptions;

import com.restaurante.domain.dto.MessageErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

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


}
