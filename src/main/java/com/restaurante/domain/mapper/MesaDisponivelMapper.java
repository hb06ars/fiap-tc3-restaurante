package com.restaurante.domain.mapper;

import com.restaurante.domain.dto.MesaDisponivelDTO;

import java.util.List;

public class MesaDisponivelMapper {
    public static List<MesaDisponivelDTO> convert(List<Object[]> mesasDisponiveis) {
        if (!mesasDisponiveis.isEmpty()) {
            return mesasDisponiveis.stream()
                    .map(result -> new MesaDisponivelDTO(
                            Long.parseLong(result[0].toString()),
                            (String) result[1],
                            (String) result[2]
                    )).toList();
        }
        return List.of();
    }
}
