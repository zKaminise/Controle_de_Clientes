package com.example.PsicoManagerProject.Entitys;

import com.example.PsicoManagerProject.Enums.EstadosEnum;
import com.example.PsicoManagerProject.Enums.GeneroEnum;
import com.example.PsicoManagerProject.Enums.RecebeuAltaEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Entity
@Table(name = "tb_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, message = "Nome não pode estar vazio")
    @Column(nullable = false)
    @Schema(example = "Gabriel Misao",minLength = 3, maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED, description = "Nome do Cliente")
    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "A data não está correta")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private GeneroEnum generoEnum;

    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadosEnum estadosEnum;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números.")
    @Column(unique = true, nullable = false)
    @Schema(example = "12345678910",minLength = 11, maxLength = 11, requiredMode = Schema.RequiredMode.REQUIRED, description = "CPF do Cliente")
    private String cpf;

    @Email(message = "Email deve ser valido")
    @Column(unique = true, nullable = false)
    @Schema(example = "Gabriel@teste.com", requiredMode = Schema.RequiredMode.REQUIRED, description = "Email do Cliente")
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números.")
    @Column(length = 11, nullable = false)
    @Schema(example = "34999999999",minLength = 10, maxLength = 11, description = "Celular do Cliente")
    private String telefone;


    private String religiao;
    private String medicamentos;

    @Column(columnDefinition = "TEXT")
    private String queixaPrincipal;

    @Enumerated(EnumType.STRING)
    @Column(name = "recebeu_alta")
    private RecebeuAltaEnum recebeuAltaEnum;
}
