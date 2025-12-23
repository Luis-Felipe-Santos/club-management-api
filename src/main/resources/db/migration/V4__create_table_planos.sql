CREATE TABLE planos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    periodicidade VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    clube_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_plano_clube
        FOREIGN KEY (clube_id) REFERENCES clubes(id),

    CONSTRAINT uk_plano_nome_clube
        UNIQUE (nome, clube_id)
);
