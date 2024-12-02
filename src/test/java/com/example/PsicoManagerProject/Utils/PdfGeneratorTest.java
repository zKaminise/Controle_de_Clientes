package com.example.PsicoManagerProject.Utils;

import com.example.PsicoManagerProject.Entitys.Client;
import com.example.PsicoManagerProject.Entitys.Financeiro;
import com.example.PsicoManagerProject.Enums.MetodoPagamentoEnum;
import com.example.PsicoManagerProject.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PdfGeneratorTest {

    @Test
    void testGenerateReport() {
        Client client = new Client();
        client.setId(1L);
        client.setNome("Gabriel Misao");

        Financeiro payment = new Financeiro();
        payment.setId(1L);
        payment.setClient(client);
        payment.setValorPago(BigDecimal.valueOf(200.50));
        payment.setDiaDoPagamento(LocalDate.now());
        payment.setMetodoPagamentoEnum(MetodoPagamentoEnum.PIX);

        assertDoesNotThrow(() -> PdfGenerator.generateReport(List.of(payment)));
    }


    @Test
    void testGenerateReceipt() {
        Client client = new Client();
        client.setId(1L);
        client.setNome("Gabriel Misao");

        Financeiro payment = new Financeiro();
        payment.setId(1L);
        payment.setClient(client);
        payment.setValorPago(BigDecimal.valueOf(200.50));
        payment.setDiaDoPagamento(LocalDate.now());
        payment.setMetodoPagamentoEnum(MetodoPagamentoEnum.PIX);

        assertDoesNotThrow(() -> PdfGenerator.generateReceipt(List.of(payment)));
    }

    @Test
    void testGenerateReceiptWithMissingFields() {
        Financeiro payment = new Financeiro();
        payment.setId(1L); // Não define outros campos obrigatórios

        assertDoesNotThrow(() -> PdfGenerator.generateReceipt(List.of(payment)));
    }


}
