package com.example.PsicoManagerProject.Dtos;

import com.example.PsicoManagerProject.Enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
