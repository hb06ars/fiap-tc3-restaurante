package com.restaurante.domain.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AjustesString {

    public static String removerTracosCpf(String cpf) {
        if (cpf != null)
            return cpf.replace("-", "").replace("(", "").replace(")", "").trim();
        return null;
    }

    public static String removerCaracteresCel(String cel) {
        if (cel != null)
            return cel.replace("-", "").replace("(", "").replace(")", "").trim();
        return null;
    }

}