package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class RecebimentoCartao implements Serializable {

    private Integer id;
    private Date data;
    private Double valorRecebido;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecebimentoCartao that = (RecebimentoCartao) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(data, that.data) &&
                Objects.equals(valorRecebido, that.valorRecebido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, valorRecebido);
    }

    @Override
    public String toString() {
        return "RecebimentoCartao{" +
                "id=" + id +
                ", data=" + data +
                ", valorRecebido=" + valorRecebido +
                '}';
    }
}
