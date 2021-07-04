package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Pagamento;
import br.com.storeapplication.model.Venda;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class PagamentoBuilder implements Builder {

    private Pagamento pagamento;

    public PagamentoBuilder() {
        this.pagamento = new Pagamento();
    }

    public PagamentoBuilder comId(Integer id) {
        pagamento.setId(id);
        return this;
    }

    public PagamentoBuilder comValor(Double valor){
        pagamento.setValor(valor);
        return this;
    }

    public PagamentoBuilder comData(Date data){
        pagamento.setData(data);
        return this;
    }

    public PagamentoBuilder comVenda(Venda venda){
        pagamento.setVenda(venda);
        return this;
    }

    public Pagamento construir() {
        return this.pagamento;
    }

    public Pagamento mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comValor(rs.getDouble("valor_pago"))
                .comData(rs.getDate("data"))
                .comVenda(new VendaBuilder().mapear(rs))
                .construir();
    }
}
