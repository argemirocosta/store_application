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

    public Double calcularValorEmAberto(Integer codvenda) {

        conexao = ConnectionFactory.getConnection();

        double valorEmAberto = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CALCULAR_VALOR_EM_ABERTO);
            ps.setInt(1, codvenda);
            ps.setInt(2, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                valorEmAberto = rs.getDouble("em_aberto");
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
        return valorEmAberto;
    }

    public Integer verificarSemPagamentos(Integer codvenda) {

        conexao = ConnectionFactory.getConnection();

        int valor = 0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_VERIFICAR_SEM_PAGAMENTOS);
            ps.setInt(1, codvenda);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                valor = rs.getInt("qtd");
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

    public List<Venda> listarVendasPorCliente() {

        conexao = ConnectionFactory.getConnection();

        List<Venda> listaVendasPorCliente = new ArrayList<>();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_LISTAR_VENDAS_POR_CLIENTE);
            ps.setInt(1, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Venda venda = new Venda();
                venda.getCliente().setNome(rs.getString("nome"));
                venda.setValor(rs.getDouble("total"));

                listaVendasPorCliente.add(venda);
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
        return listaVendasPorCliente;
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

    public Double calcularVendasTotal() {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CALCULAR_VENDAS_TOTAL);
            ps.setInt(1, usuarioSessao.getId());
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

    public Double calcularValorReceberGeral() {

        conexao = ConnectionFactory.getConnection();

        double valor = 0.0;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CALCULAR_VALOR_RECEBER_GERAL);
            ps.setInt(1, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                valor = rs.getDouble("valor");

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

    public List<Venda> listarValorAReceber() {

        conexao = ConnectionFactory.getConnection();

        List<Venda> listaValoresAReceber = new ArrayList<>();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_LISTAR_VALOR_A_RECEBER);
            ps.setInt(1, usuarioSessao.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Venda venda = new Venda();
                venda.getCliente().setNome(rs.getString("nome"));
                venda.setEmAberto(rs.getDouble("em_aberto"));
                venda.setData(rs.getDate("data"));
                venda.setValor(rs.getDouble("valor"));

                listaValoresAReceber.add(venda);
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
        return listaValoresAReceber;
    }

    public Boolean verificarSeExistePagamentoParaVenda(Integer idVenda) {

        conexao = ConnectionFactory.getConnection();

        boolean retorno = false;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_VERIFICAR_SE_EXISTE_PAGAMENTO_PARA_VENDA);
            ps.setInt(1, idVenda);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                retorno = true;
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
        return retorno;
    }

    public void cancelarVenda(Integer idVenda) {
        try {
            realizarCancelamento(idVenda, ALTERAR_CANCELAR_VENDA);
        } catch (ProjetoException e) {
            e.printStackTrace();
        }
    }

    public void cancelarPagamento(Integer idPagamento) {
        try {
            realizarCancelamento(idPagamento, ALTERAR_CANCELAR_PAGAMENTO);
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

}