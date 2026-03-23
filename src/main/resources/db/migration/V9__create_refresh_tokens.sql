CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255),
    expiry_date TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);