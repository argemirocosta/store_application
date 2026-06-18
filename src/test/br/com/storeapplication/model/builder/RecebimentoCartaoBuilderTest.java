package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.RecebimentoCartao;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecebimentoCartaoBuilderTest {

    @Test
    void deveMapearResultSetParaRecebimentoCartao() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Date data = Date.valueOf("2024-03-15");

        when(rs.getInt("id")).thenReturn(1);
        when(rs.getDate("data")).thenReturn(data);
        when(rs.getDouble("valor_recebido")).thenReturn(1400.00);

        RecebimentoCartao recebimento = new RecebimentoCartaoBuilder().mapear(rs);

        assertEquals(Integer.valueOf(1), recebimento.getId());
        assertEquals(data, recebimento.getData());
        assertEquals(Double.valueOf(1400.00), recebimento.getValorRecebido());
    }

    @Test
    void deveConstruirRecebimentoCartao() {
        Date data = Date.valueOf("2024-03-15");

        RecebimentoCartao recebimento = new RecebimentoCartaoBuilder()
                .comId(1)
                .comData(data)
                .comValorRecebido(1400.00)
                .construir();

        assertEquals(Integer.valueOf(1), recebimento.getId());
        assertEquals(data, recebimento.getData());
        assertEquals(Double.valueOf(1400.00), recebimento.getValorRecebido());
    }
}
