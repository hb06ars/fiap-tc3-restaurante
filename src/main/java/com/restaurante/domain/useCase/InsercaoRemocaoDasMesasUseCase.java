package com.restaurante.domain.useCase;

public interface InsercaoRemocaoDasMesasUseCase {
    void execute(Long idRestaurante, Integer capacidadeAtualRestaurante, Integer capacidadeAtualizada);
}