package com.restaurante.domain.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GerarNumeroProcesso {

    public static String execute(String placa) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String numeroProcesso = agora.format(formatter);
        numeroProcesso = numeroProcesso + (placa != null ? placa : gerarNumerosAleatorios());
        return numeroProcesso;
    }

    public static String gerarNumerosAleatorios() {
        String caracteresAleatorios = UUID.randomUUID().toString().replaceAll("[^A-Za-z]", "");
        if (caracteresAleatorios.length() >= 6)
            return caracteresAleatorios.substring(0, 6).toUpperCase();
        return "";
    }

}