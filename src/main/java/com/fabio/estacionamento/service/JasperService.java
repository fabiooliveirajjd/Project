package com.fabio.estacionamento.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {

    private final ResourceLoader resourceLoader; // Classe do Spring para carregar recursos
    private final DataSource dataSource; // Classe do Spring para acessar o banco de dados

    private Map<String, Object> params = new HashMap<>(); // Parâmetros para o Jasper

    private static final String JASPER_DIRETORIO = "classpath:reports/"; // Diretório onde estão os arquivos .jasper

    public void addParams(String key, Object value) {
        if (!params.containsKey("IMAGEM_DIRETORIO")) {
            this.params.put("IMAGEM_DIRETORIO", JASPER_DIRETORIO); // Parâmetro para o Jasper
            this.params.put("REPORT_LOCALE", new Locale("pt", "BR"));
        }
        this.params.put(key, value);
    }

    // Método para gerar o PDF
    public byte[] gerarPdf() {
        byte[] bytes = null; // Array de bytes para armazenar o PDF
        try {
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO.concat("estacionamento.jasper")); // Carrega o arquivo .jasper
            if (!resource.exists()) {
                throw new IOException("Report file not found: " + JASPER_DIRETORIO.concat("estacionamento.jasper"));
            }
            InputStream stream = resource.getInputStream(); // Converte o arquivo .jasper em um InputStream
            JasperPrint print = JasperFillManager.fillReport(stream, params, dataSource.getConnection()); // Preenche o relatório
            bytes = JasperExportManager.exportReportToPdf(print);  // Exporta o relatório para PDF
        } catch (IOException | JRException | SQLException e) { // Tratamento de exceções
            log.error("Error generating Jasper report. Path: {}, Params: {}, Error: {}",
                    JASPER_DIRETORIO.concat("estacionamento.jasper"), params, e.getMessage(), e); // Log de erro
            throw new RuntimeException(e); // Exceção
        }
        return bytes;
    }
}
