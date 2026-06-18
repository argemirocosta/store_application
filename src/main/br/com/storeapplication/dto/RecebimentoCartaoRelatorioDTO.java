package br.com.storeapplication.dto;

import java.util.Date;

public class RecebimentoCartaoRelatorioDTO {

    private Date dataVenda;
    private Double totalDia;
    private Double valorRecebido;
    private Double descontoJuros;
    private Double percentualTaxasJuros;

    public RecebimentoCartaoRelatorioDTO() {
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Double getTotalDia() {
        return totalDia;
    }

    public void setTotalDia(Double totalDia) {
        this.totalDia = totalDia;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Double getDescontoJuros() {
        return descontoJuros;
    }

    public void setDescontoJuros(Double descontoJuros) {
        this.descontoJuros = descontoJuros;
    }

    public Double getPercentualTaxasJuros() {
        return percentualTaxasJuros;
    }

    public void setPercentualTaxasJuros(Double percentualTaxasJuros) {
        this.percentualTaxasJuros = percentualTaxasJuros;
    }
}
