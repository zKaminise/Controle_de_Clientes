CREATE TABLE tb_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    reset_password_token VARCHAR(255),
    token_expiration_time TIMESTAMP,
    role VARCHAR(255) NOT NULL
);