package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Usuario;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsuarioBuilderTest {

    @Test
    public void testarMapearResultSetParaUsuario() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(3);
        when(rs.getString("nome")).thenReturn("Usuario Teste");
        when(rs.getString("login")).thenReturn("usuario.teste");
        when(rs.getString("senha")).thenReturn("123456");
        when(rs.getBoolean("ativo")).thenReturn(true);

        Usuario usuario = new UsuarioBuilder().mapear(rs);

        assertEquals(Integer.valueOf(3), usuario.getId());
        assertEquals("Usuario Teste", usuario.getNome());
        assertEquals("usuario.teste", usuario.getLogin());
        assertEquals("123456", usuario.getSenha());
        assertEquals(Boolean.TRUE, usuario.getAtivo());
    }

    @Test
    public void testarConstruirUsuario(){
        Usuario usuario = new UsuarioBuilder()
                .comId(3)
                .comNome("Usuario Teste")
                .comLogin("usuario.teste")
                .comSenha("123456")
                .comAtivo(true)
                .construir();

        assertEquals(Integer.valueOf(3), usuario.getId());
        assertEquals("Usuario Teste", usuario.getNome());
        assertEquals("usuario.teste", usuario.getLogin());
        assertEquals("123456", usuario.getSenha());
        assertEquals(Boolean.TRUE, usuario.getAtivo());
    }

}
