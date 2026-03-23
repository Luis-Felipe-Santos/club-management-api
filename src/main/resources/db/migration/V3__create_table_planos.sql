CREATE TABLE planos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    valor NUMERIC(19,2) NOT NULL,
    periodicidade VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    clube_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_planos_clube
        FOREIGN KEY (clube_id) REFERENCES clubes(id)
);