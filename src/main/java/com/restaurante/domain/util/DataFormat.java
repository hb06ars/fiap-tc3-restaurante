package com.restaurante.domain.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
public class DataFormat {
    public static LocalDateTime truncate(LocalDateTime dataEnviada) {
        return dataEnviada.truncatedTo(ChronoUnit.SECONDS);
    }

}