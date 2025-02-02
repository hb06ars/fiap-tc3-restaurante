package com.restaurante.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

public class TestUtil {

    private TestUtil() {
    }

    public static <T> T getRandom(Class<T> clazz) {
        var params = new EasyRandomParameters();
        params.excludeField(FieldPredicates.named("erro"));
        var generator = new EasyRandom(params);
        return generator.nextObject(clazz);
    }

    @SneakyThrows
    public static String toJsonString(Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
