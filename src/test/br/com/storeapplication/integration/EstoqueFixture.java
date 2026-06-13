package br.com.storeapplication.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Helper para inserir uma linha em vendas.estoque, necessária para
 * VendaDAO.calcularEstoque() retornar um valor diferente de zero. Não existe
 * EstoqueDAO em src/main, então este é o único lugar com INSERT via JDBC puro
 * em vez de uma DAO.
 */
public final class EstoqueFixture {

    private EstoqueFixture() {
    }

    public static void inserirValorEstoque(Integer idUsuario, double valor) throws SQLException {
        try (Connection conexao = DriverManager.getConnection(
                PostgresContainerSupport.getContainer().getJdbcUrl(),
                PostgresContainerSupport.getContainer().getUsername(),
                PostgresContainerSupport.getContainer().getPassword());
             PreparedStatement ps = conexao.prepareStatement(
                     "INSERT INTO vendas.estoque (valor, data, usuario) VALUES (?, CURRENT_DATE, ?)")) {
            ps.setDouble(1, valor);
            ps.setInt(2, idUsuario);
            ps.execute();
        }
    }
}
