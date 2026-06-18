package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.SessaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static br.com.storeapplication.shared.queries.EstoqueDAOQueries.INSERIR_ESTOQUE;

public class EstoqueDAO {

    private Connection conexao = null;

    public void inserirEstoque(Estoque estoque) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();
        Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();

        try {
            PreparedStatement ps = conexao.prepareStatement(INSERIR_ESTOQUE);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(estoque.getData()));
            ps.setDouble(2, estoque.getValor());
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
}
