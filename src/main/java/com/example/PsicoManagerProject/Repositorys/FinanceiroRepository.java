package com.example.PsicoManagerProject.Repositorys;

import com.example.PsicoManagerProject.Entitys.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {
    List<Financeiro> findByDiaDoPagamentoBetween(LocalDate startDate, LocalDate endDate);
    void deleteByClientId(Long clientId);
}
