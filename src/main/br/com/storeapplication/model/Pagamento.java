package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Pagamento implements Serializable {

    private Integer id;
    private Double valor;
    private Date data;
    private Venda venda;

    public Pagamento() {
        venda = new Venda();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id) &&
                Objects.equals(valor, pagamento.valor) &&
                Objects.equals(data, pagamento.data) &&
                Objects.equals(venda, pagamento.venda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, data, venda);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valor=" + valor +
                ", data=" + data +
                ", venda=" + venda +
                '}';
    }
}