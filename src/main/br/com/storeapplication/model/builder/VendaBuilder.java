package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.Venda;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class VendaBuilder implements Builder {

    private Venda venda;

    public VendaBuilder() {
        this.venda = new Venda();
    }

    public VendaBuilder comId(Integer id) {
        venda.setId(id);
        return this;
    }

    public VendaBuilder comValor(Double valor){
        venda.setValor(valor);
        return this;
    }

    public VendaBuilder comData(Date data){
        venda.setData(data);
        return this;
    }

    public VendaBuilder comQtd(Integer qtd) {
        venda.setQtd(qtd);
        return this;
    }

    public VendaBuilder comCliente(Cliente cliente){
        venda.setCliente(cliente);
        return this;
    }

    public VendaBuilder comFormaPagamento(FormaPagamento formaPagamento){
        venda.setFormaPagamento(formaPagamento);
        return this;
    }

    public Venda construir() {
        return this.venda;
    }

    public Venda mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comValor(rs.getDouble("valor"))
                .comData(rs.getDate("data"))
                .comQtd(rs.getInt("qtd"))
                .comCliente(new ClienteBuilder().mapear(rs))
                .comFormaPagamento(new FormaPagamentoBuilder().mapear(rs))
                .construir();
    }
}
