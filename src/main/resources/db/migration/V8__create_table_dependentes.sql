CREATE TABLE dependentes (
    id BIGSERIAL PRIMARY KEY,

    img_url TEXT,

    nome VARCHAR(150) NOT NULL,

    parentesco VARCHAR(50) NOT NULL,

    status VARCHAR(20) NOT NULL,

    socio_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_dependente_socio
        FOREIGN KEY (socio_id)
        REFERENCES socios(id)
);