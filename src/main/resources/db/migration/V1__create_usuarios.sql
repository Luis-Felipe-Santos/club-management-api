CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,

    imagem_url VARCHAR(255),

    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,

    permissao VARCHAR(50),
    status VARCHAR(50),

    clube_id BIGINT
);