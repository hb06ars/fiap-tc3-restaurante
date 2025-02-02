package com.restaurante.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

import java.util.Random;

public class TestUtil {

    private TestUtil() {
    }

    public static <T> T getRandom(Class<T> clazz) {
        var params = new EasyRandomParameters()
                .excludeField(FieldPredicates.named("erro"))
                .randomize(Integer.class, () -> new Random().nextInt(Integer.MAX_VALUE))
                .randomize(Long.class, () -> Math.abs(new Random().nextLong()))
                .randomize(Short.class, () -> (short) new Random().nextInt(Short.MAX_VALUE))
                .randomize(Byte.class, () -> (byte) new Random().nextInt(Byte.MAX_VALUE))
                .randomize(Float.class, () -> new Random().nextFloat() * Float.MAX_VALUE)
                .randomize(Double.class, () -> new Random().nextDouble() * Double.MAX_VALUE);

        var generator = new EasyRandom(params);
        return generator.nextObject(clazz);
    }

    @SneakyThrows
    public static String toJsonString(Object obj) {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
