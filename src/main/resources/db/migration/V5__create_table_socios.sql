CREATE TABLE socios (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(255) NOT NULL,

    tipo_documento VARCHAR(20) NOT NULL,
    documento VARCHAR(14) NOT NULL,

    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    endereco VARCHAR(255),
    imagem_url VARCHAR(255),

    status VARCHAR(20) NOT NULL,

    clube_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_socio_clube
        FOREIGN KEY (clube_id)
        REFERENCES clubes(id)
);

