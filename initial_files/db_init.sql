-- TABLE RESTAURANTE
CREATE TABLE IF NOT exists restaurante (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(255) NOT NULL,
                    localizacao VARCHAR(255) NOT NULL,
                    tipoCozinha VARCHAR(255)
                );



-- TABLE MESA
CREATE TABLE IF NOT exists mesa (
                    id BIGSERIAL PRIMARY KEY,
                    nomeMesa VARCHAR(255) NOT NULL,
                    restaurante_id BIGINT NOT NULL,
                    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
                );



-- TABLE USUARIO
CREATE TABLE IF NOT EXISTS usuario (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    celular VARCHAR(15)
                );


-- TABLE RESERVA
CREATE TABLE IF NOT EXISTS reserva (
                    id BIGSERIAL PRIMARY KEY,
                    usuario_id BIGINT NOT NULL,
                    mesa_id BIGINT NOT NULL,
                    data_da_reserva TIMESTAMP NOT NULL,
                    valor_reserva DECIMAL(10, 2) NOT NULL,

                    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                    CONSTRAINT fk_mesa FOREIGN KEY (mesa_id) REFERENCES mesa(id)
                );

