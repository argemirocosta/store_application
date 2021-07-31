package br.com.storeapplication.controller;

import br.com.storeapplication.util.RelatorioUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ViewScoped
@ManagedBean
public class ReportJasperMB {

    Map<String, Object> map;

    public ReportJasperMB() {
    }

    public void prepararHashMapReport(){
        map = new HashMap<>();
        map.put("REPORT_LOCALE", new Locale("pt", "BR"));
    }

    public void gerarReportVendaPorDia(Date dataInicio, Date dataFinal) throws IOException {
        String caminho = "/WEB-INF/relatorios/";
        String relatorio = caminho + "vendas_por_dia.jasper";

        prepararHashMapReport();
        map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
        map.put("datafim", new java.sql.Date(dataFinal.getTime()));
        RelatorioUtil.executeReport(relatorio, map, "Vendas por dia.pdf");
    }

    public void gerarReportVendaPorFormaDePagamento(Date dataInicio, Date dataFinal) throws IOException {
        String caminho = "/WEB-INF/relatorios/";
        String relatorio = caminho + "vendas_por_forma_de_pagamento.jasper";

        prepararHashMapReport();
        map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
        map.put("datafim", new java.sql.Date(dataFinal.getTime()));
        RelatorioUtil.executeReport(relatorio, map, "Vendas por forma de pagamento.pdf");
    }

}