package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.Venda;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VendaBuilderTest {

    @Test
    public void testarMapearResultSetParaVenda() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Date dataVenda = Date.valueOf("2024-03-10");
        Date dataNascimentoCliente = Date.valueOf("1990-05-20");

        when(rs.getInt("id")).thenReturn(15);
        when(rs.getDouble("valor")).thenReturn(199.90);
        when(rs.getDate("data")).thenReturn(dataVenda);
        when(rs.getInt("qtd")).thenReturn(3);
        when(rs.getBoolean("desconto")).thenReturn(true);
        when(rs.getDouble("percentual_desconto")).thenReturn(10.0);

        // Colunas lidas pelo ClienteBuilder (e, por sua vez, pelo EnderecoBuilder)
        when(rs.getInt("telefone2")).thenReturn(1133334444);
        when(rs.getString("nome")).thenReturn("Maria da Silva");
        when(rs.getDate("data_nascimento")).thenReturn(dataNascimentoCliente);
        when(rs.getString("cpf")).thenReturn("25623561527");
        when(rs.getString("rg")).thenReturn("123456789");
        when(rs.getString("telefone1")).thenReturn("11999998888");

        // Colunas lidas pelo FormaPagamentoBuilder
        when(rs.getString("descricao")).thenReturn("Cartão de crédito");

        Venda venda = new VendaBuilder().mapear(rs);

        assertEquals(Integer.valueOf(15), venda.getId());
        assertEquals(Double.valueOf(199.90), venda.getValor());
        assertEquals(dataVenda, venda.getData());
        assertEquals(Integer.valueOf(3), venda.getQtd());
        assertEquals(Boolean.TRUE, venda.getDesconto());
        assertEquals(Double.valueOf(10.0), venda.getPercentualDesconto());
        assertEquals("Maria da Silva", venda.getCliente().getNome());
        assertEquals("Cartão de crédito", venda.getFormaPagamento().getDescricao());
    }

    @Test
    public void testarConstruirVenda(){
        FormaPagamento formaPagamento = new FormaPagamentoBuilder().comId(2).comDescricao("Dinheiro").construir();

        Venda venda = new VendaBuilder()
                .comId(15)
                .comValor(199.90)
                .comQtd(3)
                .comFormaPagamento(formaPagamento)
                .comDesconto(false)
                .comPercentualDesconto(0.0)
                .construir();

        assertEquals(Integer.valueOf(15), venda.getId());
        assertEquals(Double.valueOf(199.90), venda.getValor());
        assertEquals(Integer.valueOf(3), venda.getQtd());
        assertEquals("Dinheiro", venda.getFormaPagamento().getDescricao());
        assertEquals(Boolean.FALSE, venda.getDesconto());
        assertEquals(Double.valueOf(0.0), venda.getPercentualDesconto());
    }

}
