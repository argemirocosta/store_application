package br.com.storeapplication.integration;

import br.com.storeapplication.factory.Propriedades;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Container Postgres único, compartilhado por toda a JVM de teste (sobe uma vez,
 * o Ryuk do Testcontainers garante o encerramento ao final da JVM).
 *
 * Carrega o schema "vendas" a partir do database.sql na raiz do repositório -
 * fonte única de verdade, não duplicar o arquivo em src/test/resources.
 */
public final class PostgresContainerSupport {

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("store_application");

    private static boolean iniciado = false;

    private PostgresContainerSupport() {
    }

    public static synchronized void iniciar() {
        if (iniciado) {
            return;
        }

        CONTAINER.start();
        carregarSchema();
        configurarPropriedadesConexao();

        iniciado = true;
    }

    private static void carregarSchema() {
        String sql;
        try {
            sql = new String(Files.readAllBytes(Paths.get("database.sql")));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler database.sql na raiz do projeto", e);
        }

        try (Connection conexao = DriverManager.getConnection(
                CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
             Statement statement = conexao.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao carregar database.sql no container Postgres", e);
        }
    }

    private static void configurarPropriedadesConexao() {
        System.setProperty("STORE_DB_URL_LOCALHOST", CONTAINER.getJdbcUrl());
        System.setProperty("DB_USUARIO_LOCALHOST", CONTAINER.getUsername());
        System.setProperty("DB_SENHA_LOCALHOST", CONTAINER.getPassword());

        Propriedades.Conexao = Propriedades.Conexoes.LOCALHOST;
    }

    public static PostgreSQLContainer<?> getContainer() {
        return CONTAINER;
    }
}
