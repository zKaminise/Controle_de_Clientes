package com.example.PsicoManagerProject.Controller;

import com.example.PsicoManagerProject.Dtos.LoginRequest;
import com.example.PsicoManagerProject.Dtos.LoginResponse;
import com.example.PsicoManagerProject.Dtos.RegisterRequest;
import com.example.PsicoManagerProject.Entitys.User;
import com.example.PsicoManagerProject.Repositorys.UserRepository;
import com.example.PsicoManagerProject.Security.JwtService;
import com.example.PsicoManagerProject.Service.EmailService;
import com.example.PsicoManagerProject.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tag(name = "Login no sistema", description = "Tela de Login e autenticação de usuarios")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

//    @PostMapping("/register")
//    @Operation(summary = "Registrar um novo usuario e senha", description = "Essa função é responsável por Registrar um novo usuario e senha")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", content = {
//                    @Content(schema = @Schema(implementation = User.class))
//            })
//    })
//    public ResponseEntity<String> register(@RequestBody User user) {
//        if (userService.isUsernameTaken(user.getUsername())) {
//            return ResponseEntity.badRequest().body("Esse Usuário já existe!");
//        }
//        userService.registerUser(user);
//        return ResponseEntity.ok("Usuário registrado com sucesso!");
//    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login no sistema", description = "Essa função é responsável por Fazer login no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Usuario não encontrado")
    })
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Usuario ou senha invalidos");
    }

    @PostMapping("/request-reset-password")
    @Operation(summary = "Solicitar um reset de senha", description = "Essa função é responsável por solicitar um reset de senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Usuario não encontrado")
    })
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
    @Operation(summary = "Alterar a senha", description = "Essa função é responsável por alterar a senha do usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = User.class))
            })
    })
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
