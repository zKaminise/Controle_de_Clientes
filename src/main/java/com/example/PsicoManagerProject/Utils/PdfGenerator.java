package com.example.PsicoManagerProject.Utils;

import com.example.PsicoManagerProject.Entitys.Financeiro;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
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
                map.put("nome", payment.getClient() != null ? payment.getClient().getNome() : "N/A"); // Adiciona o nome do cliente
                map.put("referencia", payment.getReferencia() != null ? payment.getReferencia() : "N/A");
                return map;
            }).toList();

            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/financeiroReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório PDF: " + e.getMessage());
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
                map.put("nome", payment.getClient() != null ? payment.getClient().getNome() : "N/A"); // Adiciona o nome do cliente
                map.put("referencia", payment.getReferencia() != null ? payment.getReferencia() : "N/A"); // Adiciona a referência
                return map;
            }).toList();

            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/reciboTemplate.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório PDF: " + e.getMessage());
        }
    }




}
