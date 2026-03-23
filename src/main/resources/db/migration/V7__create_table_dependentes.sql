CREATE TABLE dependentes (
    id BIGSERIAL PRIMARY KEY,
    img_url VARCHAR(255),
    nome VARCHAR(255) NOT NULL,
    parentesco VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    socio_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_dependentes_socio
        FOREIGN KEY (socio_id) REFERENCES socios(id)
);