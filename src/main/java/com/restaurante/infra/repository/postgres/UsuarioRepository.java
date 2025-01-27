package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email OR u.celular = :celular")
    UsuarioEntity findByEmailOrCelular(@Param("email") String email, @Param("celular") String celular);

}