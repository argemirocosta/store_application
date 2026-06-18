package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.PostgresContainerSupport;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
import br.com.storeapplication.integration.UsuarioFixture;
import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.builder.CaixaDiarioBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaixaDiarioDAOIT extends PostgresIntegrationTestBase {

    private final CaixaDiarioDAO caixaDiarioDAO = new CaixaDiarioDAO();

    private Usuario usuario;

    @BeforeEach
    void setUp() throws ProjetoException {
        usuario = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuario);
    }

    @Test
    void deveInserirCaixaDiario() throws ProjetoException, SQLException {
        CaixaDiario caixaDiario = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(350.00)
                .construir();

        caixaDiarioDAO.inserirCaixaDiario(caixaDiario);

        assertEquals(1, contarRegistros(usuario.getId()));
    }

    @Test
    void naoDeveEnxergarCaixaDiarioDeOutroUsuario() throws ProjetoException, SQLException {
        CaixaDiario caixaDiarioA = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(200.00)
                .construir();
        caixaDiarioDAO.inserirCaixaDiario(caixaDiarioA);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        CaixaDiario caixaDiarioB = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(100.00)
                .construir();
        caixaDiarioDAO.inserirCaixaDiario(caixaDiarioB);

        assertEquals(1, contarRegistros(usuario.getId()));
        assertEquals(1, contarRegistros(usuarioB.getId()));
    }

    @Test
    void deveBuscarDiferencaCaixaSomenteDoUsuarioLogado() throws ProjetoException {
        CaixaDiario caixaDiarioA = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(200.00)
                .construir();
        caixaDiarioDAO.inserirCaixaDiario(caixaDiarioA);

        CaixaDiario caixaDiarioB = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(100.00)
                .construir();
        caixaDiarioDAO.inserirCaixaDiario(caixaDiarioB);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        CaixaDiario caixaDiarioC = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(999.00)
                .construir();
        caixaDiarioDAO.inserirCaixaDiario(caixaDiarioC);

        loginComoUsuario(usuario);

        Double diferenca = caixaDiarioDAO.buscarDiferencaCaixa();

        assertEquals(300.00, diferenca, 0.001);
    }

    private int contarRegistros(int usuarioId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                PostgresContainerSupport.getContainer().getJdbcUrl(),
                PostgresContainerSupport.getContainer().getUsername(),
                PostgresContainerSupport.getContainer().getPassword());
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM vendas.caixa_diario WHERE usuario = ?")) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
