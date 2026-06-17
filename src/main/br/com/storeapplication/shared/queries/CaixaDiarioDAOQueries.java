package br.com.storeapplication.shared.queries;

public class CaixaDiarioDAOQueries {

    private CaixaDiarioDAOQueries() {
    }

    public static final String INSERT_CAIXA_DIARIO =
            "INSERT INTO vendas.caixa_diario (data, valor, usuario) VALUES (?,?,?)";
}
