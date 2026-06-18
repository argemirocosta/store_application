package br.com.storeapplication.shared.queries;

public class RecebimentosCartaoDAOQueries {

    private RecebimentosCartaoDAOQueries() {
    }

    public static final String SELECT_DESCONTO_CARTAO =
            "SELECT sum(valor_total) AS valor_vendido_cartao, "
            + "sum(valor_recebido) AS valor_recebido_cartao, "
            + "sum(taxas_juros) AS valor_descontado_cartao "
            + "FROM ( "
            + "SELECT (SELECT sum(vv.valor) FROM vendas.venda vv WHERE vv.\"data\" = v.\"data\" "
            + "AND vv.cancelada IS NOT TRUE AND vv.id_forma_pagamento IN (2,4) AND vv.usuario = ?) AS valor_total, "
            + "rc.valor_recebido, sum(v.valor) - rc.valor_recebido AS taxas_juros, "
            + "v.\"data\" AS data_venda, rc.\"data\" AS data_recebimento "
            + "FROM vendas.\"recebimentos_cartão\" rc "
            + "INNER JOIN vendas.venda v ON (rc.\"data\" = v.\"data\") "
            + "WHERE v.id_forma_pagamento IN (2,4) "
            + "AND v.usuario = ? "
            + "AND rc.\"data\" >= ? AND rc.\"data\" <= ? "
            + "AND v.cancelada IS NOT TRUE "
            + "AND rc.usuario = ? "
            + "GROUP BY v.\"data\", rc.valor_recebido, rc.\"data\" "
            + "ORDER BY rc.\"data\" "
            + ") total";
}
