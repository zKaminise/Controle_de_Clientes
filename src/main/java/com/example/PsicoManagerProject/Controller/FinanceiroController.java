package com.example.PsicoManagerProject.Controller;

import com.example.PsicoManagerProject.Dtos.PaymentRequest;
import com.example.PsicoManagerProject.Entitys.Financeiro;
import com.example.PsicoManagerProject.Exceptions.ClientNotFoundException;
import com.example.PsicoManagerProject.Exceptions.ResourceNotFoundException;
import com.example.PsicoManagerProject.Repositorys.ClientRepository;
import com.example.PsicoManagerProject.Repositorys.FinanceiroRepository;
import com.example.PsicoManagerProject.Utils.PdfGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/financeiro")
@Tag(name = "Financeiro", description = "Inforamções de pagamentos")
public class FinanceiroController {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @Operation(summary = "Cadastrar pagamento do Cliente, informando o CPF", description = "Essa função é responsável por cadastrar novos pagamentos dos clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Financeiro.class))
            })
    })
    public String addPayment(@RequestBody PaymentRequest paymentRequest) {
        var client = clientRepository.findByCpf(paymentRequest.getCpf())
                .orElseThrow(() -> new ClientNotFoundException("Cliente com CPF: " + paymentRequest.getCpf() + " Não Encontrado"));

        Financeiro payment = new Financeiro();
        payment.setClient(client);
        payment.setValorPago(paymentRequest.getValorPago());
        payment.setDiaDoPagamento(paymentRequest.getDiaDoPagamento());
        payment.setMetodoPagamentoEnum(paymentRequest.getMetodoPagamentoEnum());

        financeiroRepository.save(payment);
        return "Pagamento Adicionado com sucesso!";
    }


    @GetMapping("/report")
    @Operation(summary = "Relatório dos pagamentos cadastrados", description = "Essa função é responsável por trazer o relatório dos pagamentos dos clientes, filtrados por datas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Financeiro.class))
            })
    })
    public String generateReport(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Financeiro> payments = financeiroRepository.findByDiaDoPagamentoBetween(start, end);
        PdfGenerator.generateReport(payments);
        return "Relatório gerado com sucesso!";
    }

    @GetMapping("/receipt/{id}")
    @Operation(summary = "Emitir recibo de pagamento do cliente", description = "Essa função é responsável por Emitir recibo de pagamento do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Financeiro.class))
            })
    })
    public ResponseEntity<String> generateReceipt(@PathVariable Long id) {
        Financeiro payment = financeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento com ID: " + id + " não foi encontrado!"));

        try {
            PdfGenerator.generateReceipt(List.of(payment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar recibo: " + e.getMessage());
        }

        return ResponseEntity.ok("Recibo gerado com sucesso!");
    }

}
