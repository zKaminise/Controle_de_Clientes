CREATE TABLE tb_financeiro (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL REFERENCES tb_cliente(id),
    valor_pago NUMERIC(10, 2),
    dia_do_pagamento DATE,
    referencia TEXT,
    pagamento_via VARCHAR(255)
);