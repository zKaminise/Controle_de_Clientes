package com.example.PsicoManagerProject.Entitys;

import com.example.PsicoManagerProject.Enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "zKaminise", description = "Usuario para Login")
    private String username;

    @Schema(example = "senha123", description = "Senha para Login")
    private String password;

    private String resetPasswordToken;
    private LocalDateTime tokenExpirationTime;

    @Schema(example = "ADMIN", description = "Admin caso seja um usuario adminstrador, ou USER caso seja somente usuario final")
    @Enumerated(EnumType.STRING)
    private Role role;
}