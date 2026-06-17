package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CaixaDiarioBuilder implements Builder {

    private CaixaDiario caixaDiario;

    public CaixaDiarioBuilder() {
        this.caixaDiario = new CaixaDiario();
    }

    public CaixaDiarioBuilder comId(Integer id) {
        caixaDiario.setId(id);
        return this;
    }

    public CaixaDiarioBuilder comData(Date data) {
        caixaDiario.setData(data);
        return this;
    }

    public CaixaDiarioBuilder comValor(Double valor) {
        caixaDiario.setValor(valor);
        return this;
    }

    public CaixaDiario construir() {
        return this.caixaDiario;
    }

    public CaixaDiario mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comData(rs.getDate("data"))
                .comValor(rs.getDouble("valor"))
                .construir();
    }
}
