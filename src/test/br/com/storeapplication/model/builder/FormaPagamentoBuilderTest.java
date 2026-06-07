package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.FormaPagamento;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormaPagamentoBuilderTest {

    @Test
    public void testarMapearResultSetParaFormaPagamento() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(2);
        when(rs.getString("descricao")).thenReturn("Cartão de crédito");

        FormaPagamento formaPagamento = new FormaPagamentoBuilder().mapear(rs);

        assertEquals(Integer.valueOf(2), formaPagamento.getId());
        assertEquals("Cartão de crédito", formaPagamento.getDescricao());
    }

    @Test
    public void testarConstruirFormaPagamento(){
        FormaPagamento formaPagamento = new FormaPagamentoBuilder()
                .comId(2)
                .comDescricao("Cartão de crédito")
                .construir();

        assertEquals(Integer.valueOf(2), formaPagamento.getId());
        assertEquals("Cartão de crédito", formaPagamento.getDescricao());
    }

}
