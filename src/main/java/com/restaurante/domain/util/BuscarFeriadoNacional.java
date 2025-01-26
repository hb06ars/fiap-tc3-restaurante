package com.restaurante.domain.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BuscarFeriadoNacional {
    public static boolean consultar(LocalDate diaSelecionado) {
        List<LocalDate> feriados = Arrays.asList(
                LocalDate.of(diaSelecionado.getYear(), 1, 1),   // Ano Novo
                LocalDate.of(diaSelecionado.getYear(), 4, 21),  // Tiradentes
                LocalDate.of(diaSelecionado.getYear(), 5, 1),   // Dia do Trabalhador
                LocalDate.of(diaSelecionado.getYear(), 9, 7),   // Independência do Brasil
                LocalDate.of(diaSelecionado.getYear(), 10, 12), // Nossa Senhora Aparecida
                LocalDate.of(diaSelecionado.getYear(), 11, 2),  // Finados
                LocalDate.of(diaSelecionado.getYear(), 11, 15), // Proclamação da República
                LocalDate.of(diaSelecionado.getYear(), 12, 25)  // Natal
        );
        return feriados.contains(diaSelecionado);
    }
}
