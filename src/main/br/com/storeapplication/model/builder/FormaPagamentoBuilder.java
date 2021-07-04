package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FormaPagamentoBuilder implements Builder {

    private FormaPagamento formaPagamento;

    public FormaPagamentoBuilder() {
        this.formaPagamento = new FormaPagamento();
    }

    public FormaPagamentoBuilder comId(Integer id) {
        formaPagamento.setId(id);
        return this;
    }

    public FormaPagamentoBuilder comDescricao(String descricao){
        formaPagamento.setDescricao(descricao);
        return this;
    }

    public FormaPagamento construir() {
        return this.formaPagamento;
    }

    public FormaPagamento mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comDescricao(rs.getString("descricao"))
                .construir();
    }
}
