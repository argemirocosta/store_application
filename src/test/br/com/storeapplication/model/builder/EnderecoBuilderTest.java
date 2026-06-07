package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Endereco;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnderecoBuilderTest {

    @Test
    public void testarMapearResultSetParaEndereco() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("cep")).thenReturn("01001-000");
        when(rs.getString("estado")).thenReturn("SP");
        when(rs.getString("cidade")).thenReturn("São Paulo");
        when(rs.getString("bairro")).thenReturn("Sé");
        when(rs.getString("logradouro")).thenReturn("Praça da Sé");
        when(rs.getInt("numero")).thenReturn(100);
        when(rs.getInt("cod_ibge")).thenReturn(3550308);

        Endereco endereco = new EnderecoBuilder().mapear(rs);

        assertEquals("01001-000", endereco.getCep());
        assertEquals("SP", endereco.getEstado());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("Sé", endereco.getBairro());
        assertEquals("Praça da Sé", endereco.getLogradouro());
        assertEquals(Integer.valueOf(100), endereco.getNumero());
        assertEquals(Integer.valueOf(3550308), endereco.getCodIBGE());
    }

    @Test
    public void testarConstruirEndereco(){
        Endereco endereco = new EnderecoBuilder()
                .comCep("01001-000")
                .comEstado("SP")
                .comCidade("São Paulo")
                .comBairro("Sé")
                .comLogradouro("Praça da Sé")
                .comNumero(100)
                .comCodIBGE(3550308)
                .construir();

        assertEquals("01001-000", endereco.getCep());
        assertEquals("SP", endereco.getEstado());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("Sé", endereco.getBairro());
        assertEquals("Praça da Sé", endereco.getLogradouro());
        assertEquals(Integer.valueOf(100), endereco.getNumero());
        assertEquals(Integer.valueOf(3550308), endereco.getCodIBGE());
    }

}
