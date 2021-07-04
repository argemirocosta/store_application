package br.com.storeapplication.shared.queries;

public class FormasPagamentoDAOQueries {

    private FormasPagamentoDAOQueries() {
    }

    public static final String SELECT_LISTAR_FORMAS_PAGAMENTO = "SELECT id, descricao "
            + "FROM vendas.forma_pagamento "
            + "ORDER BY descricao";

}
