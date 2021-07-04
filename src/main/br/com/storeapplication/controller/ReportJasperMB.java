package br.com.storeapplication.controller;

import br.com.storeapplication.util.RelatorioUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ViewScoped
@ManagedBean
public class ReportJasperMB {

    public ReportJasperMB() {
    }

    public void gerarRecibo(Integer idPagamento) throws IOException {
        String caminho = "/WEB-INF/relatorios/";
        String relatorio = caminho + "recibo.jasper";
        Map<String, Object> map = new HashMap<>();

        map.put("id_pagamento", idPagamento);
        map.put("REPORT_LOCALE", new Locale("pt", "BR"));
        RelatorioUtil.executeReport(relatorio, map, "Recibo venda "+idPagamento+".pdf");
    }

}