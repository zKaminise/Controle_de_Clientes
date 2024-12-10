//package com.example.PsicoManagerProject.Utils;
//
//import com.example.PsicoManagerProject.Entitys.Client;
//import com.example.PsicoManagerProject.Entitys.Financeiro;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.io.ByteArrayOutputStream;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PdfGeneratorTest {
//
//    @Test
//    void testGenerateReceipt() {
//        // Dados simulados
//        Client cliente = new Client();
//        cliente.setNome("João da Silva");
//        cliente.setCpf("123.456.789-00");
//
//        Financeiro pagamento = new Financeiro();
//        pagamento.setValorPago(new BigDecimal("100.00"));
//        pagamento.setDiaDoPagamento(LocalDate.of(2024, 12, 10));
//        pagamento.setClient(cliente);
//        pagamento.setReferencia("Dezembro");
//        pagamento.setMetodoPagamentoEnum(Financeiro.MetodoPagamentoEnum.PIX);
//
//        List<Financeiro> pagamentos = new ArrayList<>();
//        pagamentos.add(pagamento);
//
//        try (MockedStatic<JasperCompileManager> jasperCompileManagerMock = mockStatic(JasperCompileManager.class);
//             MockedStatic<JasperFillManager> jasperFillManagerMock = mockStatic(JasperFillManager.class);
//             MockedStatic<JasperExportManager> jasperExportManagerMock = mockStatic(JasperExportManager.class)) {
//
//            // Mock dos métodos estáticos do JasperReports
//            JasperReport mockReport = mock(JasperReport.class);
//            JasperPrint mockPrint = mock(JasperPrint.class);
//
//            jasperCompileManagerMock.when(() -> JasperCompileManager.compileReport(anyString()))
//                    .thenReturn(mockReport);
//
//            jasperFillManagerMock.when(() -> JasperFillManager.fillReport(eq(mockReport), any(), any(JRBeanCollectionDataSource.class)))
//                    .thenReturn(mockPrint);
//
//            jasperExportManagerMock.when(() -> JasperExportManager.exportReportToPdfStream(eq(mockPrint), any()))
//                    .thenAnswer(invocation -> {
//                        ByteArrayOutputStream outputStream = invocation.getArgument(1);
//                        outputStream.write("PDF Teste".getBytes());
//                        return null;
//                    });
//
//            // Execução do método
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PdfGenerator.generateReceipt(pagamentos, outputStream);
//
//            // Verificações
//            assertNotNull(outputStream);
//            assertTrue(outputStream.size() > 0);
//            String pdfContent = outputStream.toString();
//            assertTrue(pdfContent.contains("PDF Teste"));
//
//            jasperCompileManagerMock.verify(() -> JasperCompileManager.compileReport(anyString()), times(1));
//            jasperFillManagerMock.verify(() -> JasperFillManager.fillReport(eq(mockReport), any(), any(JRBeanCollectionDataSource.class)), times(1));
//            jasperExportManagerMock.verify(() -> JasperExportManager.exportReportToPdfStream(eq(mockPrint), any()), times(1));
//        } catch (Exception e) {
//            fail("Erro inesperado: " + e.getMessage());
//        }
//    }
//}
