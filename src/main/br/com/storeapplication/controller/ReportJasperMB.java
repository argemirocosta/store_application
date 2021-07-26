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

    public ReportJasperMB() {
    }

    public void gerarReportVendaPorDia(Date dataInicio, Date dataFinal) throws IOException {
        String caminho = "/WEB-INF/relatorios/";
        String relatorio = caminho + "vendas_por_dia.jasper";
        Map<String, Object> map = new HashMap<>();

        map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
        map.put("datafim", new java.sql.Date(dataFinal.getTime()));
        map.put("REPORT_LOCALE", new Locale("pt", "BR"));
        RelatorioUtil.executeReport(relatorio, map, "Vendas por dia.pdf");
    }

    public void gerarReportVendaPorFormaDePagamento(Date dataInicio, Date dataFinal) throws IOException {
        String caminho = "/WEB-INF/relatorios/";
        String relatorio = caminho + "vendas_por_forma_de_pagamento.jasper";
        Map<String, Object> map = new HashMap<>();

        map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
        map.put("datafim", new java.sql.Date(dataFinal.getTime()));
        map.put("REPORT_LOCALE", new Locale("pt", "BR"));
        RelatorioUtil.executeReport(relatorio, map, "Vendas por forma de pagamento.pdf");
    }

}