package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Cliente;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClienteBuilderTest {

    @Test
    public void testarMapearResultSetParaCliente() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Date dataNascimento = Date.valueOf("1990-05-20");

        when(rs.getInt("id")).thenReturn(7);
        when(rs.getString("nome")).thenReturn("Maria da Silva");
        when(rs.getDate("data_nascimento")).thenReturn(dataNascimento);
        when(rs.getString("cpf")).thenReturn("25623561527");
        when(rs.getString("rg")).thenReturn("123456789");
        when(rs.getString("telefone1")).thenReturn("11999998888");
        when(rs.getInt("telefone2")).thenReturn(1133334444);

        // Colunas lidas pelo EnderecoBuilder, delegado por ClienteBuilder.mapear
        when(rs.getString("cep")).thenReturn("01001-000");
        when(rs.getString("estado")).thenReturn("SP");
        when(rs.getString("cidade")).thenReturn("São Paulo");
        when(rs.getString("bairro")).thenReturn("Sé");
        when(rs.getString("logradouro")).thenReturn("Praça da Sé");
        when(rs.getInt("numero")).thenReturn(100);
        when(rs.getInt("cod_ibge")).thenReturn(3550308);

        Cliente cliente = new ClienteBuilder().mapear(rs);

        assertEquals(Integer.valueOf(7), cliente.getId());
        assertEquals("Maria da Silva", cliente.getNome());
        assertEquals(dataNascimento, cliente.getDataNascimento());
        assertEquals("25623561527", cliente.getCpf());
        assertEquals("123456789", cliente.getRg());
        assertEquals("11999998888", cliente.getTelefone1());
        assertEquals(Integer.valueOf(1133334444), cliente.getTelefone2());
        assertEquals("SP", cliente.getEndereco().getEstado());
        assertEquals("São Paulo", cliente.getEndereco().getCidade());
        assertEquals(Integer.valueOf(3550308), cliente.getEndereco().getCodIBGE());
    }

    @Test
    public void testarConstruirCliente(){
        Cliente cliente = new ClienteBuilder()
                .comId(7)
                .comNome("Maria da Silva")
                .comCpf("25623561527")
                .comTelefone1("11999998888")
                .construir();

        assertEquals(Integer.valueOf(7), cliente.getId());
        assertEquals("Maria da Silva", cliente.getNome());
        assertEquals("25623561527", cliente.getCpf());
        assertEquals("11999998888", cliente.getTelefone1());
    }

}
