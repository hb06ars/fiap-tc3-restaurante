package com.restaurante.domain.useCase;

public interface InserirRemoverMesasUseCase {
    void execute(Long idRestaurante, Integer capacidadeAtualRestaurante, Integer capacidadeAtualizada);
}