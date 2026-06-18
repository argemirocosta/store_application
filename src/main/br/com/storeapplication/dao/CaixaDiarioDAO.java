package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.SessaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.storeapplication.shared.queries.CaixaDiarioDAOQueries.BUSCAR_VALOR_CAIXA;
import static br.com.storeapplication.shared.queries.CaixaDiarioDAOQueries.INSERIR_CAIXA_DIARIO;

public class CaixaDiarioDAO {

    private Connection conexao = null;

    public void inserirCaixaDiario(CaixaDiario caixaDiario) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(INSERIR_CAIXA_DIARIO);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(caixaDiario.getData()));
            ps.setDouble(2, caixaDiario.getValor());
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

    public Double buscarDiferencaCaixa() {
        conexao = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conexao.prepareStatement(BUSCAR_VALOR_CAIXA);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("diferenca_caixa");
            }
            return 0.0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
