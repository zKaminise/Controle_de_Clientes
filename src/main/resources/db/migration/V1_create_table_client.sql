CREATE TABLE tb_cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE NOT NULL,
    genero VARCHAR(255),
    endereco VARCHAR(255),
    estado VARCHAR(255),
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    telefone CHAR(11) NOT NULL,
    religiao VARCHAR(255),
    medicamentos VARCHAR(255),
    tratamento TEXT,
    queixa_principal TEXT,
    frequencia TEXT,
    escolaridade VARCHAR(255),
    data_inicio_tratamento DATE NOT NULL,
    data_fim_tratamento DATE
);