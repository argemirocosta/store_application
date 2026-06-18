package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.PostgresContainerSupport;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
import br.com.storeapplication.integration.UsuarioFixture;
import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.builder.EstoqueBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstoqueDAOIT extends PostgresIntegrationTestBase {

    private final EstoqueDAO estoqueDAO = new EstoqueDAO();

    private Usuario usuario;

    @BeforeEach
    void setUp() throws ProjetoException {
        usuario = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuario);
    }

    @Test
    void deveInserirEstoque() throws ProjetoException, SQLException {
        Estoque estoque = new EstoqueBuilder()
                .comData(new Date())
                .comValor(1000.00)
                .construir();

        estoqueDAO.inserirEstoque(estoque);

        assertEquals(1, contarRegistros(usuario.getId()));
    }

    @Test
    void naoDeveEnxergarEstoqueDeOutroUsuario() throws ProjetoException, SQLException {
        Estoque estoqueA = new EstoqueBuilder()
                .comData(new Date())
                .comValor(500.00)
                .construir();
        estoqueDAO.inserirEstoque(estoqueA);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        Estoque estoqueB = new EstoqueBuilder()
                .comData(new Date())
                .comValor(250.00)
                .construir();
        estoqueDAO.inserirEstoque(estoqueB);

        assertEquals(1, contarRegistros(usuario.getId()));
        assertEquals(1, contarRegistros(usuarioB.getId()));
    }

    private int contarRegistros(int usuarioId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                PostgresContainerSupport.getContainer().getJdbcUrl(),
                PostgresContainerSupport.getContainer().getUsername(),
                PostgresContainerSupport.getContainer().getPassword());
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM vendas.estoque WHERE usuario = ?")) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
