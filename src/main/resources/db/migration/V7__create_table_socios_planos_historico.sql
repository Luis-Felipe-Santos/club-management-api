CREATE TABLE socios_planos_historico (
    id BIGSERIAL PRIMARY KEY,

    socio_plano_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,

    status_anterior VARCHAR(20),
    status_novo VARCHAR(20) NOT NULL,
    acao VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historico_socio_plano
        FOREIGN KEY (socio_plano_id)
        REFERENCES socios_planos(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_historico_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);
