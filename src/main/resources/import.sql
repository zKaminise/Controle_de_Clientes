INSERT INTO tb_cliente (nome, data_nascimento, genero, endereco, estado, cpf, email, telefone, religiao, medicamentos, tratamento, queixa_principal, frequencia, escolaridade, data_Inicio_Tratamento, data_Fim_Tratamento) VALUES ('Gabriel Misao', '1990-05-15', 'MASCULINO', 'Rua das Flores, 123', 'MG', '12345678910', 'gabriel@teste.com', '34999999999', 'Cristianismo', 'Aspirina', 'teste', 'Dor de cabeça constante.', '2x na semana', 'ensinoSuperior', '2024-12-03', NULL);
INSERT INTO tb_cliente (nome, data_nascimento, genero, endereco, estado, cpf, email, telefone, religiao, medicamentos, tratamento, queixa_principal, frequencia, escolaridade, data_Inicio_Tratamento, data_Fim_Tratamento) VALUES ('Ana Clara', '1985-08-20', 'FEMININO', 'Av. Paulista, 456', 'SP', '98765432100', 'ana@teste.com', '11987654321', 'Ateísmo', 'Nenhum', 'teste', 'Ansiedade generalizada.', '4x na semana', 'ensinoSuperior', '2024-01-10', NULL);

INSERT INTO tb_financeiro (client_id, valor_pago, dia_do_pagamento, pagamento_via) VALUES (1, 200.50, '2024-12-01', 'PIX');
INSERT INTO tb_financeiro (client_id, valor_pago, dia_do_pagamento, pagamento_via) VALUES (2, 150.75, '2024-11-30', 'CARTAO');

INSERT INTO tb_user (username, password, reset_password_token, token_expiration_time, role) VALUES ('admin', '$2a$10$82MYt7eKuS0uCF2uNAI7cuNNyhnrN4X1Ve5cgOHUSASVR84IEFzGS', NULL, NULL, 'ADMIN');
INSERT INTO tb_user (username, password, reset_password_token, token_expiration_time, role) VALUES ('gabriel', '$2a$10$ocvwmi5N6ixLIElpBp/Wnui7dsyLO7qiZSxG9wPPdT850I9BNfT.W', NULL, NULL, 'USER');
