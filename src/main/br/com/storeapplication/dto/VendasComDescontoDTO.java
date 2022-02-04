package br.com.storeapplication.dto;

public class VendasComDescontoDTO {

    private Double soma;
    private Double percentualDesconto;

    public VendasComDescontoDTO() {

    }

    public Double getSoma() {
        return soma;
    }

    public void setSoma(Double soma) {
        this.soma = soma;
    }

    public Double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(Double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }
}