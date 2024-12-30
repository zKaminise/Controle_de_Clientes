package com.example.PsicoManagerProject.Utils;

import com.example.PsicoManagerProject.Entitys.Financeiro;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

            InputStream templateStream = PdfGenerator.class.getResourceAsStream("/templates/financeiroReport.jrxml");
            if (templateStream == null) {
                throw new FileNotFoundException("Template 'financeiroReport.jrxml' não encontrado no classpath.");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

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
                map.put("nome", payment.getClient() != null ? payment.getClient().getNome() : "N/A"); // Adiciona o nome do cliente
                map.put("referencia", payment.getReferencia() != null ? payment.getReferencia() : "N/A"); // Adiciona a referência
                return map;
            }).toList();

            InputStream templateStream = PdfGenerator.class.getResourceAsStream("templates/reciboTemplate.jrxml");
            if (templateStream == null) {
                System.err.println("Erro: Template 'reciboTemplate.jrxml' não encontrado no classpath.");
                throw new FileNotFoundException("Template 'reciboTemplate.jrxml' não encontrado no classpath.");
            } else {
                System.out.println("Template encontrado com sucesso!");

            }
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

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
