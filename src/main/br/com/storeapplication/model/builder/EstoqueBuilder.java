package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EstoqueBuilder implements Builder {

    private Estoque estoque;

    public EstoqueBuilder() {
        this.estoque = new Estoque();
    }

    public EstoqueBuilder comId(Integer id) {
        estoque.setId(id);
        return this;
    }

    public EstoqueBuilder comData(Date data) {
        estoque.setData(data);
        return this;
    }

    public EstoqueBuilder comValor(Double valor) {
        estoque.setValor(valor);
        return this;
    }

    public Estoque construir() {
        return this.estoque;
    }

    public Estoque mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comData(rs.getDate("data"))
                .comValor(rs.getDouble("valor"))
                .construir();
    }
}
