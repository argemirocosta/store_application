package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.Venda;
import br.com.storeapplication.model.builder.ClienteBuilder;
import br.com.storeapplication.model.builder.FormaPagamentoBuilder;
import br.com.storeapplication.model.builder.VendaBuilder;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.SessaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.storeapplication.shared.queries.VendaDAOQueries.*;
import static br.com.storeapplication.shared.Sessao.*;

public class VendaDAO {

    private Connection conexao = null;

    private Usuario usuarioSessao = (Usuario) SessaoUtil.resgatarDaSessao(USUARIO_SESSAO);

    public void inserirVenda(Venda venda) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conexao.prepareStatement(INSERIR_VENDA);
            ps.setInt(1, venda.getCliente().getId());
            ps.setDouble(2, venda.getValor());
            ps.setInt(3, venda.getQtd());
            ps.setDate(4, DataUtil.converterDateUtilParaDateSql(venda.getData()));
            ps.setInt(5, usuarioSessao.getId());
            ps.setInt(6, venda.getFormaPagamento().getId());

            ps.execute();

            conexao.commit();

        } catch (SQLException ex) {
            throw new ProjetoException(ex);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<Venda> listarVendas(Venda vendaParametro) {

        conexao = ConnectionFactory.getConnection();

        List<Venda> listaVendas = new ArrayList<>();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_LISTAR_VENDAS);

            ps.setInt(1, vendaParametro.getCliente().getId());
            ResultSet rs = ps.executeQuery();

            listaVendas = mapearResultSetListaVendas(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaVendas;
    }

    private List<Venda> mapearResultSetListaVendas(ResultSet rs) {

        ArrayList<Venda> listaVendas = new ArrayList<>();

        try {
            while (rs.next()) {
                VendaBuilder vendaBuilder = new VendaBuilder();
                listaVendas.add(vendaBuilder.comId(rs.getInt("id"))
                        .comData(rs.getDate("data"))
                        .comValor(rs.getDouble("valor"))
                        .comQtd(rs.getInt("qtd"))
                        .comCliente(new ClienteBuilder().comId(rs.getInt("id_cliente")).comNome(rs.getString("nome")).construir())
                        .comFormaPagamento(new FormaPagamentoBuilder().comId(rs.getInt("id_forma_pagamento")).comDescricao(rs.getString("descricao")).construir())
                        .construir());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaVendas;
    }

    public Double consultarVendasPorPeriodo(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CONSULTAR_VENDAS_POR_PERIODO);
            ps.setDate(1,
                    new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(2, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ps.setInt(3, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                valor = rs.getDouble("soma");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return valor;
    }

    public Double consultarMediaDiaria(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CONSULTAR_MEDIA_DIARIA);
            ps.setDate(1,
                    new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(2, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ps.setInt(3, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                valor = rs.getDouble("media_diaria");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return valor;
    }

    public Double calcularEstoque() {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CALCULAR_ESTOQUE);
            ps.setInt(1, usuarioSessao.getId());
            ps.setInt(2, usuarioSessao.getId());
            ps.setInt(3, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                valor = rs.getDouble("valor_estoque");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return valor;
    }

    public void cancelarVenda(Integer idVenda) {
        try {
            realizarCancelamento(idVenda, ALTERAR_CANCELAR_VENDA);
        } catch (ProjetoException e) {
            e.printStackTrace();
        }
    }

    private void realizarCancelamento(Integer idParaCancelar, String sqlCancelamento) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conexao.prepareStatement(sqlCancelamento);
            ps.setInt(1, idParaCancelar);

            ps.execute();

            conexao.commit();

        } catch (SQLException ex) {
            throw new ProjetoException(ex);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Double consultarValorMercadoriaParaRepor(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CONSULTAR_VALOR_A_REPOR_MERCADORIA);
            ps.setInt(1, usuarioSessao.getId());
            ps.setDate(2,
                    new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ps.setInt(4, usuarioSessao.getId());
            ps.setDate(5,
                    new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(6, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                valor = rs.getDouble("repor_mercadoria");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return valor;
    }

}