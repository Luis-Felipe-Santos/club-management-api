CREATE TABLE socios_planos (
    id BIGSERIAL PRIMARY KEY,
    socio_id BIGINT NOT NULL,
    plano_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_socios_planos_socio
        FOREIGN KEY (socio_id) REFERENCES socios(id),
    CONSTRAINT fk_socios_planos_plano
        FOREIGN KEY (plano_id) REFERENCES planos(id)
);