package com.restaurante.infra.config.postgres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@Profile("test")
@Slf4j
public class TestDatabaseConfig {

    private final JdbcTemplate jdbcTemplate;

    public TestDatabaseConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public void clearDatabase() {
        try {
            String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/clean.sql")));
            jdbcTemplate.execute(sql);
            log.info("Limpeza do Banco de dados H2 finalizada.");
        } catch (Exception e) {
            log.error("Erro na limpeza do Banco de dados H2.");
        }
    }
}

