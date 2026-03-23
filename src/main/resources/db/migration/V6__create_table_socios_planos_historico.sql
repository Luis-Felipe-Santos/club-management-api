CREATE TABLE socios_planos_historico (
    id BIGSERIAL PRIMARY KEY,
    socio_plano_id BIGINT NOT NULL,
    status_anterior VARCHAR(50) NOT NULL,
    status_novo VARCHAR(50) NOT NULL,
    acao VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_socios_planos_historico_socio_plano
        FOREIGN KEY (socio_plano_id) REFERENCES socios_planos(id),
    CONSTRAINT fk_socios_planos_historico_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);