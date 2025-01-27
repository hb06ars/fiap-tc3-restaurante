package com.restaurante.domain.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AjustesString {

    public static String removerTracosCpf(String cpf) {
        return cpf.replace("-", "").replace("/", "").replace(".", "").trim();
    }

    public static String removerCaracteresCel(String cel) {
        return cel.replace("-", "").replace("(", "").replace(")", "").trim();
    }

}