package com.example.PsicoManagerProject.Repositorys;

import com.example.PsicoManagerProject.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByResetPasswordToken(String token);
}
