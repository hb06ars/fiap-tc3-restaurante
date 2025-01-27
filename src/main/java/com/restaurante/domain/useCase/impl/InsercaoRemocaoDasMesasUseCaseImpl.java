package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.useCase.InsercaoRemocaoDasMesasUseCase;
import com.restaurante.infra.exceptions.CapacidadeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class InsercaoRemocaoDasMesasUseCaseImpl implements InsercaoRemocaoDasMesasUseCase {

    private final MesaService mesaService;

    public InsercaoRemocaoDasMesasUseCaseImpl(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @Override
    public void execute(Long idRestaurante, Integer capacidadeOriginal, Integer capacidadeAtualizada) {
        if (!Objects.equals(capacidadeOriginal, capacidadeAtualizada)) {
            if (capacidadeAtualizada > 0) {
                if (capacidadeOriginal > capacidadeAtualizada) {
                    removerMesas(capacidadeOriginal - capacidadeAtualizada, idRestaurante);
                } else {
                    adicionarMesas(capacidadeAtualizada - capacidadeOriginal, idRestaurante);
                }
            } else {
                throw new CapacidadeException("A capacidade deve ser maior que zero.");
            }
        }
    }

    private void removerMesas(int quantidadeDeMesas, Long idRestaurante) {
        List<MesaDTO> todasMesas = mesaService.findAllByIdRestaurante(idRestaurante);
        for (int i = 0; i < quantidadeDeMesas; i++) {
            mesaService.delete(todasMesas.get(i).getId());
        }
    }

    private void adicionarMesas(int quantidadeDeMesas, Long idRestaurante) {
        int totalMesas = mesaService.findAllByIdRestaurante(idRestaurante).size();
        List<MesaEntity> novasMesasAdicionadas = new ArrayList<>();
        for (int i = 0; i < quantidadeDeMesas; i++) {
            MesaEntity novaMesa = new MesaEntity();
            novaMesa.setRestauranteId(idRestaurante);
            novaMesa.setNomeMesa("MESA " + totalMesas + i);
            novasMesasAdicionadas.add(novaMesa);
        }
        if (!novasMesasAdicionadas.isEmpty())
            mesaService.salvarTodasMesas(novasMesasAdicionadas);
    }
}
