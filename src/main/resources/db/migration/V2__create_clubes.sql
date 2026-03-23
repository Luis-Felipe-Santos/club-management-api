CREATE TABLE clubes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL,
    usuario_admin_id BIGINT NOT NULL,
    CONSTRAINT fk_clubes_usuario_admin
        FOREIGN KEY (usuario_admin_id) REFERENCES usuarios(id)
);