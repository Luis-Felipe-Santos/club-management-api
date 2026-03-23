CREATE TABLE pagamentos (
    id BIGSERIAL PRIMARY KEY,
    socio_plano_id BIGINT NOT NULL,
    competencia VARCHAR(7) NOT NULL,
    valor_base NUMERIC(19,2) NOT NULL,
    valor_final NUMERIC(19,2) NOT NULL,
    desconto NUMERIC(19,2),
    acrescimo NUMERIC(19,2),
    status VARCHAR(50) NOT NULL,
    data_vencimento DATE,
    data_pagamento DATE,
    observacao VARCHAR(255),
    CONSTRAINT fk_pagamentos_socio_plano
        FOREIGN KEY (socio_plano_id) REFERENCES socios_planos(id),
    CONSTRAINT uk_pagamentos_socio_plano_competencia
        UNIQUE (socio_plano_id, competencia)
);