package com.restaurante.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {

    protected ObjectMapper objectMapper;

    @BeforeAll
    protected void before() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    protected static <T> T getRandom(Class<T> clazz) {
        return TestUtil.getRandom(clazz);
    }

    protected String toJsonString(Object obj) {
        return TestUtil.toJsonString(obj);
    }

}
