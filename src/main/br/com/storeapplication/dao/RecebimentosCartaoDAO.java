package br.com.storeapplication.dao;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.SessaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.storeapplication.shared.queries.RecebimentosCartaoDAOQueries.*;

public class RecebimentosCartaoDAO {

    private Connection conexao = null;

    public DescontoCartaoDTO consultarDescontoCartao(BuscaRelatorio busca) {

        conexao = ConnectionFactory.getConnection();
        DescontoCartaoDTO dto = new DescontoCartaoDTO();
        dto.setValorCartaoPeriodo(0.0);
        dto.setValorRecebido(0.0);
        dto.setValorDescontoCartao(0.0);
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_DESCONTO_CARTAO);
            ps.setInt(1, usuarioSessao.getId());
            ps.setInt(2, usuarioSessao.getId());
            ps.setDate(3, new java.sql.Date(busca.getPeriodoinicial().getTime()));
            ps.setDate(4, DataUtil.converterDateUtilParaDateSql(busca.getPeriodofinal()));
            ps.setInt(5, usuarioSessao.getId());
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
