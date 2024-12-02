package com.example.PsicoManagerProject.Dtos;

import com.example.PsicoManagerProject.Enums.MetodoPagamentoEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {

    private String cpf;
    private BigDecimal valorPago;
    private LocalDate diaDoPagamento;
    private MetodoPagamentoEnum metodoPagamentoEnum;
}
