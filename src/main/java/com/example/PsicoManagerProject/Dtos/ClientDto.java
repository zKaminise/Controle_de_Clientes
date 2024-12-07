package com.example.PsicoManagerProject.Dtos;

import lombok.Data;

@Data
public class ClientDto {
    private String nome;
    private String cpf;
    private String email;
    private String dataNascimento;
    private String recebeuAlta;

    public ClientDto(String nome, String cpf, String email, String dataNascimento, String recebeuAlta) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.recebeuAlta = recebeuAlta;
    }
}
