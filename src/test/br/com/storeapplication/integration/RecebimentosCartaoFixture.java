package br.com.storeapplication.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public final class RecebimentosCartaoFixture {

    private RecebimentosCartaoFixture() {
    }

    public static void inserirRecebimentoCartao(Date data, double valorRecebido) throws SQLException {
        try (Connection conexao = DriverManager.getConnection(
                PostgresContainerSupport.getContainer().getJdbcUrl(),
                PostgresContainerSupport.getContainer().getUsername(),
                PostgresContainerSupport.getContainer().getPassword());
             PreparedStatement ps = conexao.prepareStatement(
                     "INSERT INTO vendas.\"recebimentos_cartão\" (\"data\", valor_recebido) VALUES (?, ?)")) {
            ps.setDate(1, new java.sql.Date(data.getTime()));
            ps.setDouble(2, valorRecebido);
            ps.execute();
        }
    }
}
