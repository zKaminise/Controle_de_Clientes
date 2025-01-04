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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
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

    @GetMapping("/{cpf}/pagamentos")
    @Operation(summary = "Listar pagamentos por CPF", description = "Essa função é responsável por listar todos os pagamentos cadastrados para um cliente específico usando o CPF.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Financeiro.class))
            }),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CPF fornecido")
    })
    public ResponseEntity<List<Financeiro>> getPaymentsByCpf(@PathVariable String cpf) {
        var client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com CPF: " + cpf + " não encontrado"));

        List<Financeiro> payments = financeiroRepository.findByClient(client);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/receipt/{cpf}/{month}/{year}")
    @Operation(summary = "Gerar recibo por mês e ano", description = "Gera um recibo para o cliente com base no CPF, mês e ano fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recibo gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum pagamento encontrado para o cliente no mês/ano especificado")
    })
    public ResponseEntity<byte[]> generateReceiptByMonthAndYear(
            @PathVariable String cpf,
            @PathVariable int month,
            @PathVariable int year) {
        var client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com CPF: " + cpf + " não encontrado"));

        // Calcula o intervalo de datas
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Financeiro> payments = financeiroRepository.findByClientAndDiaDoPagamentoBetween(client, startDate, endDate);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum pagamento encontrado para o cliente no mês/ano especificado");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfGenerator.generateReceipt(payments, baos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename("recibo.pdf").build());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar recibo", e);
        }
    }




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
        payment.setReferencia(paymentRequest.getReferencia());
        payment.setMetodoPagamentoEnum(paymentRequest.getMetodoPagamentoEnum());

        financeiroRepository.save(payment);
        return "Pagamento Adicionado com sucesso!";
    }


    @GetMapping("/report")
    @Operation(summary = "Relatório dos pagamentos cadastrados", description = "Gera um relatório dos pagamentos dos clientes, filtrados por datas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Nenhum pagamento encontrado no intervalo de datas fornecido")
    })
    public ResponseEntity<byte[]> generateReport(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            // Converte as datas recebidas como String para LocalDate
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Busca os pagamentos no intervalo de datas
            List<Financeiro> payments = financeiroRepository.findByDiaDoPagamentoBetween(start, end);
            if (payments.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum pagamento encontrado no intervalo de datas fornecido.");
            }

            // Prepara o intervalo de datas para o relatório
            String dataIntervalo = startDate + " a " + endDate;

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                // Passa o intervalo de datas para o PdfGenerator
                PdfGenerator.generateReport(payments, baos, dataIntervalo);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.inline().filename("relatorio.pdf").build());

                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Editar pagamento", description = "Atualiza um pagamento existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Financeiro.class))
            }),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    public ResponseEntity<Financeiro> updatePayment(
            @PathVariable Long id, @RequestBody PaymentRequest paymentRequest) {
        Financeiro payment = financeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento com ID " + id + " não encontrado"));

        payment.setValorPago(paymentRequest.getValorPago());
        payment.setDiaDoPagamento(paymentRequest.getDiaDoPagamento());
        payment.setReferencia(paymentRequest.getReferencia());
        payment.setMetodoPagamentoEnum(paymentRequest.getMetodoPagamentoEnum());

        financeiroRepository.save(payment);
        return ResponseEntity.ok(payment);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pagamento", description = "Exclui um pagamento pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        Financeiro payment = financeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento com ID " + id + " não encontrado"));
        financeiroRepository.delete(payment);
        return ResponseEntity.ok().build();
    }


}
