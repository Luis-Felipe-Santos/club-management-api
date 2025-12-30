CREATE TABLE pagamentos (
    id BIGSERIAL PRIMARY KEY,

    socio_plano_id BIGINT NOT NULL,

    competencia VARCHAR(7) NOT NULL,

    valor_base NUMERIC(10,2) NOT NULL,
    valor_final NUMERIC(10,2) NOT NULL,

    desconto NUMERIC(10,2),
    acrescimo NUMERIC(10,2),

    status VARCHAR(20) NOT NULL,

    data_vencimento DATE,
    data_pagamento DATE,

    observacao TEXT,

    CONSTRAINT fk_pagamento_socio_plano
        FOREIGN KEY (socio_plano_id)
        REFERENCES socios_planos(id),

    CONSTRAINT uk_pagamento_competencia
        UNIQUE (socio_plano_id, competencia)
);