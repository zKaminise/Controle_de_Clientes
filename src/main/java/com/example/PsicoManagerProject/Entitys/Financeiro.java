package com.example.PsicoManagerProject.Entitys;

import com.example.PsicoManagerProject.Enums.MetodoPagamentoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_financeiro")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Financeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @Schema(example = "199.50", description = "Valor pago pelo cliente")
    private BigDecimal valorPago;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(example = "2024-11-08", description = "Dia que o pagamento foi feito")
    private LocalDate diaDoPagamento;

    @Column(columnDefinition = "TEXT")
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "Pagamento_via")
    @Schema(example = "PIX", description = "Método usado para pagar")
    private MetodoPagamentoEnum metodoPagamentoEnum;

    @Column(name = "endereco", columnDefinition = "TEXT")
    private String endereco;


    public String getMetodoPagamentoAsString() {
        return metodoPagamentoEnum.name();
    }

}
