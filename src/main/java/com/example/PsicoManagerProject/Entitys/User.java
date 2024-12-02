package com.example.PsicoManagerProject.Entitys;

import com.example.PsicoManagerProject.Enums.Role;
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

    private String username;
    private String password;

    private String resetPasswordToken;
    private LocalDateTime tokenExpirationTime;

    @Enumerated(EnumType.STRING)
    private Role role;
}