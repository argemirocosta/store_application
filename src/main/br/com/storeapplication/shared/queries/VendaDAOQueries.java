package br.com.storeapplication.shared.queries;

public class VendaDAOQueries {

    private VendaDAOQueries() {
    }

    public static final String INSERIR_VENDA = "INSERT INTO vendas.venda (id_cliente, valor, qtd, data, usuario, id_forma_pagamento) " +
            "VALUES (?,?,?,?,?,?)";

    public static final String SELECT_LISTAR_VENDAS = "SELECT v.id, v.id_cliente, c.nome, v.data, " +
            "v.valor, v.qtd, v.id_forma_pagamento, fp.descricao " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id)" +
            "LEFT JOIN vendas.clientes c ON (v.id_cliente = c.id) " +
            "WHERE v.id_cliente = ? AND v.cancelada IS NOT TRUE " +
            "ORDER BY v.data DESC, v.id DESC";

    public static final String SELECT_LISTAR_VENDAS_POR_CLIENTE = "SELECT v.id_cliente, c.nome, sum(v.valor) AS total "
            + "FROM vendas.venda v "
            + "LEFT JOIN vendas.clientes c ON (v.id_cliente = c.id) "
            + "WHERE v.usuario = ? AND v.cancelada IS NOT TRUE "
            + "GROUP BY v.id_cliente, c.nome "
            + "ORDER BY total DESC";

    public static final String SELECT_CONSULTAR_VENDAS_POR_PERIODO = "SELECT sum(valor) AS soma FROM vendas.venda WHERE DATA BETWEEN ? AND ? AND usuario = ? "
            + "AND cancelada IS NOT TRUE";

    public static final String SELECT_CALCULAR_VENDAS_TOTAL = "SELECT sum(valor) AS soma FROM vendas.venda WHERE usuario = ? AND cancelada IS NOT TRUE";

    public static final String ALTERAR_CANCELAR_VENDA = "UPDATE vendas.venda SET cancelada = TRUE, data_hora_cancelamento = CURRENT_TIMESTAMP WHERE id=?";

    public static final String SELECT_CALCULAR_ESTOQUE = "SELECT sum(valor) - " +
            "((SELECT sum(valor) FROM vendas.venda v WHERE usuario = ? AND cancelada IS NOT TRUE) / 2) " +
            "AS valor_estoque " +
            "FROM vendas.estoque e " +
            "WHERE usuario = ?";

}
