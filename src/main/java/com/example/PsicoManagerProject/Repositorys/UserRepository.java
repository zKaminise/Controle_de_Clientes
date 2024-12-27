package com.example.PsicoManagerProject.Repositorys;

import com.example.PsicoManagerProject.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByResetPasswordToken(String token);
}
