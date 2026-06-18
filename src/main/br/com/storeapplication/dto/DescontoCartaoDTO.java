package br.com.storeapplication.dto;

public class DescontoCartaoDTO {

    private Double valorCartaoPeriodo;
    private Double valorRecebido;
    private Double valorDescontoCartao;

    public DescontoCartaoDTO() {
    }

    public Double getValorCartaoPeriodo() {
        return valorCartaoPeriodo;
    }

    public void setValorCartaoPeriodo(Double valorCartaoPeriodo) {
        this.valorCartaoPeriodo = valorCartaoPeriodo;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Double getValorDescontoCartao() {
        return valorDescontoCartao;
    }

    public void setValorDescontoCartao(Double valorDescontoCartao) {
        this.valorDescontoCartao = valorDescontoCartao;
    }
}
