package br.com.storeapplication.dao;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.dto.RecebimentoCartaoRelatorioDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.SessaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static br.com.storeapplication.shared.queries.RecebimentosCartaoDAOQueries.*;

public class RecebimentosCartaoDAO {

    private Connection conexao = null;

    public void inserirRecebimentoCartao(RecebimentoCartao recebimento) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(INSERIR_RECEBIMENTO_CARTAO);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(recebimento.getData()));
            ps.setDouble(2, recebimento.getValorRecebido());
            ps.setInt(3, usuarioSessao.getId());

            ps.executeUpdate();

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

    public ArrayList<RecebimentoCartaoRelatorioDTO> consultarRecebimentosCartao(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();
        ArrayList<RecebimentoCartaoRelatorioDTO> lista = new ArrayList<>();
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_RECEBIMENTOS_CARTAO_RELATORIO);
            ps.setInt(1, usuarioSessao.getId());
            ps.setInt(2, usuarioSessao.getId());
            ps.setDate(3, new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(4, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RecebimentoCartaoRelatorioDTO dto = new RecebimentoCartaoRelatorioDTO();
                dto.setDataVenda(rs.getDate("data_venda"));
                dto.setTotalDia(rs.getDouble("total_dia"));
                dto.setValorRecebido(rs.getDouble("valor_recebido"));
                dto.setDescontoJuros(rs.getDouble("desconto_juros"));
                dto.setPercentualTaxasJuros(rs.getDouble("percentual_taxas_juros"));
                lista.add(dto);
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
        return lista;
    }

    public DescontoCartaoDTO consultarDescontoCartao(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();
        DescontoCartaoDTO dto = new DescontoCartaoDTO();
        dto.setValorCartaoPeriodo(0.0);
        dto.setValorRecebido(0.0);
        dto.setValorDescontoCartao(0.0);
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_CONSULTAR_DESCONTO_CARTAO);
            ps.setInt(1, usuarioSessao.getId());
            ps.setInt(2, usuarioSessao.getId());
            ps.setDate(3, new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(4, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto.setValorCartaoPeriodo(rs.getDouble("valor_vendido_cartao"));
                dto.setValorRecebido(rs.getDouble("valor_recebido_cartao"));
                dto.setValorDescontoCartao(rs.getDouble("valor_descontado_cartao"));
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
        return dto;
    }
}
