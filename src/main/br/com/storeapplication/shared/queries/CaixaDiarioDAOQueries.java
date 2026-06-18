package br.com.storeapplication.shared.queries;

public class CaixaDiarioDAOQueries {

    private CaixaDiarioDAOQueries() {
    }

    public static final String INSERIR_CAIXA_DIARIO =
            "INSERT INTO vendas.caixa_diario (data, valor, usuario) VALUES (?,?,?)";

    public static final String BUSCAR_VALOR_CAIXA =
            "SELECT sum(valor) AS diferenca_caixa FROM vendas.caixa_diario";
}
