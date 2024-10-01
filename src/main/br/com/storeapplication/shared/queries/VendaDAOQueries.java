package br.com.storeapplication.shared.queries;

public class VendaDAOQueries {

    private VendaDAOQueries() {
    }

    public static final String INSERIR_VENDA = "INSERT INTO vendas.venda (id_cliente, valor, qtd, data, usuario, " +
            "id_forma_pagamento, desconto, percentual_desconto) " +
            "VALUES (?,?,?,?,?,?,?,?)";

    public static final String SELECT_LISTAR_VENDAS = "SELECT v.id, v.id_cliente, c.nome, v.data, " +
            "v.valor, v.qtd, v.id_forma_pagamento, fp.descricao, desconto, percentual_desconto " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id)" +
            "LEFT JOIN vendas.clientes c ON (v.id_cliente = c.id) " +
            "WHERE v.id_cliente = ? AND v.cancelada IS NOT TRUE " +
            "ORDER BY v.data DESC, v.id DESC";

    public static final String SELECT_CONSULTAR_VENDAS_POR_PERIODO_SEM_DESCONTO =
            "SELECT sum(valor) AS soma FROM vendas.venda WHERE DATA BETWEEN ? AND ? AND usuario = ? "
            + "AND cancelada IS NOT TRUE AND desconto IS NOT TRUE";

    public static final String SELECT_CONSULTAR_VENDAS_POR_PERIODO_COM_DESCONTO =
            "SELECT sum(valor) AS soma, percentual_desconto FROM vendas.venda WHERE DATA BETWEEN ? AND ? AND usuario = ? "
                    + "AND cancelada IS NOT TRUE AND desconto IS TRUE GROUP BY percentual_desconto";

    public static final String ALTERAR_CANCELAR_VENDA = "UPDATE vendas.venda SET cancelada = TRUE, data_hora_cancelamento = CURRENT_TIMESTAMP WHERE id=?";

    public static final String SELECT_CALCULAR_ESTOQUE = "SELECT (sum(valor) " +
            "- " +
            "((SELECT sum(valor) " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id) " +
            "WHERE usuario = ? AND cancelada IS NOT TRUE " +
            "AND v.desconto IS NOT TRUE AND fp.credito IS NOT TRUE) / 2) " +
            "- " +
            "((SELECT sum(valor)/100*90 " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id) " +
            "WHERE usuario = ? AND cancelada IS NOT TRUE " +
            "AND v.desconto IS NOT TRUE AND fp.credito IS TRUE) / 2) " +
            "- " +
            "(SELECT sum(valor) FROM vendas.venda v " +
            "WHERE usuario = ? AND cancelada IS NOT TRUE " +
            "AND v.desconto IS TRUE)) " +
            "* 1.33 AS valor_estoque " +
            "FROM vendas.estoque e " +
            "WHERE usuario = ?";

    public static final String SELECT_CONSULTAR_MEDIA_DIARIA_COLECAO = "SELECT sum(dias.total)/count(dias.datas) AS media_diaria " +
            "FROM ( " +
            "SELECT v.\"data\" AS datas, sum(valor) AS total " +
            "FROM vendas.venda v " +
            "WHERE v.\"data\" >= ? AND v.\"data\" <= ? " +
            "AND v.cancelada IS NOT TRUE " +
            "AND v.usuario = ? " +
            "AND v.desconto IS NOT TRUE " +
            "GROUP BY v.\"data\" " +
            "ORDER BY \"data\" " +
            ") dias";

    public static final String SELECT_CONSULTAR_MEDIA_DIARIA_PROMOCAO = "SELECT sum(dias.total)/count(dias.datas) AS media_diaria " +
            "FROM ( " +
            "SELECT v.\"data\" AS datas, sum(valor) AS total " +
            "FROM vendas.venda v " +
            "WHERE v.\"data\" >= ? AND v.\"data\" <= ? " +
            "AND v.cancelada IS NOT TRUE " +
            "AND v.usuario = ? " +
            "AND v.desconto IS TRUE " +
            "GROUP BY v.\"data\" " +
            "ORDER BY \"data\" " +
            ") dias";

    public static final String SELECT_CONSULTAR_VALOR_A_REPOR_MERCADORIA = "SELECT " +
            "((SELECT sum(valor) " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id) " +
            "WHERE usuario = ? AND v.\"data\" >= ? AND v.\"data\" <= ? " +
            "AND cancelada IS NOT TRUE AND fp.credito IS NOT TRUE) / 2) " +
            "+ " +
            "((SELECT sum(valor)/100*90  " +
            "FROM vendas.venda v " +
            "JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id) " +
            "WHERE usuario = ? AND v.\"data\" >= ? AND v.\"data\" <= ? " +
            "AND cancelada IS NOT TRUE AND fp.credito IS TRUE) / 2) " +
            "AS repor_mercadoria ";

}
