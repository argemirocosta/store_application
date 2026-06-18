package br.com.storeapplication.shared.queries;

public class EstoqueDAOQueries {

    private EstoqueDAOQueries() {
    }

    public static final String INSERIR_ESTOQUE =
            "INSERT INTO vendas.estoque (data, valor, usuario) VALUES (?,?,?)";
}
