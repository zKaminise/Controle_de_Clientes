package com.example.PsicoManagerProject.Repositorys;

import com.example.PsicoManagerProject.Entitys.Client;
import com.example.PsicoManagerProject.Entitys.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro,Long> {
    List<Financeiro> findByDiaDoPagamentoBetween(LocalDate startDate, LocalDate endDate);
    List<Financeiro> findByClientAndDiaDoPagamentoBetween(Client client, LocalDate start, LocalDate end);
    void deleteByClientId(Long clientId);
    List<Financeiro> findByClient(Client client);

    @Query("SELECT f FROM Financeiro f WHERE f.client = :client AND MONTH(f.diaDoPagamento) = :month AND YEAR(f.diaDoPagamento) = :year")
    List<Financeiro> findByClientAndMonthAndYear(Client client, int month, int year);


}
