package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RecebimentoCartaoBuilder implements Builder {

    private RecebimentoCartao recebimentoCartao;

    public RecebimentoCartaoBuilder() {
        this.recebimentoCartao = new RecebimentoCartao();
    }

    public RecebimentoCartaoBuilder comId(Integer id) {
        recebimentoCartao.setId(id);
        return this;
    }

    public RecebimentoCartaoBuilder comData(Date data) {
        recebimentoCartao.setData(data);
        return this;
    }

    public RecebimentoCartaoBuilder comValorRecebido(Double valorRecebido) {
        recebimentoCartao.setValorRecebido(valorRecebido);
        return this;
    }

    public RecebimentoCartao construir() {
        return this.recebimentoCartao;
    }

    public RecebimentoCartao mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comData(rs.getDate("data"))
                .comValorRecebido(rs.getDouble("valor_recebido"))
                .construir();
    }

}
