package com.example.PsicoManagerProject.Controller;

import com.example.PsicoManagerProject.Dtos.LoginRequest;
import com.example.PsicoManagerProject.Dtos.LoginResponse;
import com.example.PsicoManagerProject.Dtos.RegisterRequest;
import com.example.PsicoManagerProject.Entitys.User;
import com.example.PsicoManagerProject.Repositorys.UserRepository;
import com.example.PsicoManagerProject.Security.JwtService;
import com.example.PsicoManagerProject.Service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private EmailService emailService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return "Usuario Registrado com Sucesso!";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Usuario ou senha invalidos");
    }

    @PostMapping("/request-reset-password")
    public String requestResetPassword(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "Usuario não encontrado!";
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getUsername(), token);

        return "Email de redefinir senha foi enviado!";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null || user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return "Token invalido ou expirado!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setTokenExpirationTime(null);
        userRepository.save(user);

        return "Senha alterada com sucesso!";
    }
}
