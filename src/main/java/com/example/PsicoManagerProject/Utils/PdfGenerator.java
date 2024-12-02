package com.example.PsicoManagerProject.Utils;

import com.example.PsicoManagerProject.Entitys.Financeiro;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public static void generateReport(List<Financeiro> payments) {
        try {
            // Converte o enum para String no DTO
            List<Map<String, Object>> data = payments.stream().map(payment -> {
                Map<String, Object> map = new HashMap<>();
                map.put("metodoPagamentoEnum", payment.getMetodoPagamentoEnum() != null ? payment.getMetodoPagamentoEnum().name() : "N/A");
                map.put("diaDoPagamento", payment.getDiaDoPagamento());
                map.put("valorPago", payment.getValorPago());
                return map;
            }).toList();

            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/financeiroReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            // Salvar o arquivo diretamente se estiver em um ambiente headless
            if (GraphicsEnvironment.isHeadless()) {
                String testFilePath = "target/test-outputs/relatorio_financeiro.pdf";
                new File("target/test-outputs").mkdirs(); // Cria a pasta, se necessário
                JasperExportManager.exportReportToPdfFile(jasperPrint, testFilePath);
                System.out.println("PDF gerado automaticamente para testes: " + testFilePath);
            } else {
                // Ambiente com interface gráfica
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar Relatório");
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setSelectedFile(new File("relatorio_financeiro.pdf"));

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".pdf")) {
                        filePath += ".pdf"; // Garante a extensão .pdf
                    }
                    JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
                    System.out.println("PDF gerado com sucesso: " + filePath);
                } else {
                    System.out.println("Operação de salvamento cancelada pelo usuário.");
                }
            }

        } catch (JRException e) {
            e.printStackTrace();
            System.err.println("Erro ao gerar o relatório PDF: " + e.getMessage());
        }
    }


    public static void generateReceipt(Financeiro payment) {
        generateReceipt(List.of(payment));
    }

    public static void generateReceipt(List<Financeiro> payments) {
        try {
            List<Map<String, Object>> data = payments.stream().map(payment -> {
                Map<String, Object> map = new HashMap<>();
                map.put("metodoPagamentoEnum", payment.getMetodoPagamentoEnum() != null ? payment.getMetodoPagamentoEnum().name() : "N/A");
                map.put("diaDoPagamento", payment.getDiaDoPagamento());
                map.put("valorPago", payment.getValorPago());
                return map;
            }).toList();


            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/reciboTemplate.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            // Salvar o arquivo diretamente se estiver em um ambiente headless
            if (GraphicsEnvironment.isHeadless()) {
                String testFilePath = "target/test-outputs/recibo.pdf";
                new File("target/test-outputs").mkdirs(); // Cria a pasta, se necessário
                JasperExportManager.exportReportToPdfFile(jasperPrint, testFilePath);
                System.out.println("PDF gerado automaticamente: " + testFilePath);
            } else {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar Recibo");
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setSelectedFile(new File("recibo.pdf"));

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".pdf")) {
                        filePath += ".pdf";
                    }
                    JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
                    System.out.println("PDF gerado com sucesso: " + filePath);
                } else {
                    System.out.println("Operação de salvamento cancelada pelo usuário.");
                }
            }

        } catch (JRException e) {
            e.printStackTrace();
            System.err.println("Erro ao gerar o relatório PDF: " + e.getMessage());
        }
    }

}
