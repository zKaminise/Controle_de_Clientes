package com.example.PsicoManagerProject.Utils;

import com.example.PsicoManagerProject.Entitys.Financeiro;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public static void generateReport(List<Financeiro> payments, OutputStream outputStream) {
        try {
            List<Map<String, Object>> data = payments.stream().map(payment -> {
                Map<String, Object> map = new HashMap<>();
                map.put("metodoPagamentoEnum", payment.getMetodoPagamentoEnum() != null ? payment.getMetodoPagamentoEnum().name() : "N/A");
                map.put("diaDoPagamento", payment.getDiaDoPagamento());
                map.put("valorPago", payment.getValorPago());
                map.put("nome", payment.getClient() != null ? payment.getClient().getNome() : "N/A");
                map.put("referencia", payment.getReferencia() != null ? payment.getReferencia() : "N/A");
                return map;
            }).toList();

            // Obtém o caminho do template a partir da variável de ambiente
            String templatePath = System.getenv("RECIBO_TEMPLATE_PATH");
            if (templatePath == null || templatePath.isEmpty()) {
                throw new IllegalStateException("A variável de ambiente RECIBO_TEMPLATE_PATH não está configurada.");
            }

            // Localiza o arquivo do template
            File templateFile = new File(templatePath, "financeiroReport.jrxml");
            if (!templateFile.exists()) {
                throw new FileNotFoundException("Arquivo 'financeiroReport.jrxml' não encontrado no caminho: " + templateFile.getAbsolutePath());
            }

            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(templateFile.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório PDF: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateReceipt(List<Financeiro> payments, OutputStream outputStream) {
        try {
            List<Map<String, Object>> data = payments.stream().map(payment -> {
                Map<String, Object> map = new HashMap<>();
                map.put("metodoPagamentoEnum", payment.getMetodoPagamentoEnum() != null ? payment.getMetodoPagamentoEnum().name() : "N/A");
                map.put("diaDoPagamento", payment.getDiaDoPagamento());
                map.put("valorPago", payment.getValorPago());
                map.put("cpf", payment.getClient() != null ? payment.getClient().getCpf() : "N/A");
                map.put("nome", payment.getClient() != null ? payment.getClient().getNome() : "N/A");
                map.put("referencia", payment.getReferencia() != null ? payment.getReferencia() : "N/A");
                return map;
            }).toList();

            // Obtém o caminho do template a partir da variável de ambiente
            String templatePath = System.getenv("RECIBO_TEMPLATE_PATH");
            if (templatePath == null || templatePath.isEmpty()) {
                throw new IllegalStateException("A variável de ambiente RECIBO_TEMPLATE_PATH não está configurada.");
            }

            // Localiza o arquivo do template
            File templateFile = new File(templatePath, "reciboTemplate.jrxml");
            if (!templateFile.exists()) {
                throw new FileNotFoundException("Arquivo 'reciboTemplate.jrxml' não encontrado no caminho: " + templateFile.getAbsolutePath());
            }

            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(templateFile.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório PDF: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
