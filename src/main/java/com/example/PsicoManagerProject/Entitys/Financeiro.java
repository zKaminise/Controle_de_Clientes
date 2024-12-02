package com.example.PsicoManagerProject.Entitys;

import com.example.PsicoManagerProject.Enums.MetodoPagamentoEnum;
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

    private BigDecimal valorPago;
    private LocalDate diaDoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "Pagamento_via")
    private MetodoPagamentoEnum metodoPagamentoEnum;

    public String getMetodoPagamentoAsString() {
        return metodoPagamentoEnum.name();
    }

}
