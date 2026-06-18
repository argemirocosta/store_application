package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Estoque;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EstoqueBuilderTest {

    @Test
    void deveMapearResultSetParaEstoque() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Date data = Date.valueOf("2024-03-15");

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getDate("data")).thenReturn(data);
        when(rs.getDouble("valor")).thenReturn(1500.00);

        Estoque estoque = new EstoqueBuilder().mapear(rs);

        assertEquals(Integer.valueOf(1), estoque.getId());
        assertEquals(data, estoque.getData());
        assertEquals(Double.valueOf(1500.00), estoque.getValor());
    }

    @Test
    void deveConstruirEstoque() {
        Date data = Date.valueOf("2024-03-15");

        Estoque estoque = new EstoqueBuilder()
                .comId(1)
                .comData(data)
                .comValor(1500.00)
                .construir();

        assertEquals(Integer.valueOf(1), estoque.getId());
        assertEquals(data, estoque.getData());
        assertEquals(Double.valueOf(1500.00), estoque.getValor());
    }
}
