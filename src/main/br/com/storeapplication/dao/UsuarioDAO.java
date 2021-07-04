package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.dto.ParametrosVerificarSenhaUsuarioDTO;
import br.com.storeapplication.model.builder.UsuarioBuilder;
import br.com.storeapplication.util.SessaoUtil;
import br.com.storeapplication.util.VerificadorUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.storeapplication.shared.queries.UsuarioDAOQueries.*;
import static br.com.storeapplication.shared.Sessao.*;

public class UsuarioDAO {

    private Connection conexao;

    public Usuario login(Usuario usuarioLogin) {

        conexao = ConnectionFactory.getConnection();

        Usuario usuarioRetorno = null;

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_LOGIN);
            ps.setString(1, usuarioLogin.getLogin().toUpperCase());
            ps.setString(2, usuarioLogin.getSenha());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usuarioRetorno = new UsuarioBuilder().mapear(rs);
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(usuarioRetorno)) {
                SessaoUtil.adicionarNaSessao(usuarioRetorno, USUARIO_SESSAO);
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
        return usuarioRetorno;
    }

    public void inserirUsuario(Usuario usuario) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conexao.prepareStatement(INSERIR_USUARIO);
            ps.setString(1, usuario.getNome().toUpperCase());
            ps.setString(2, usuario.getLogin().toUpperCase());
            ps.setString(3, usuario.getSenha());

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

    public void alterarUsuario(Usuario usuario) throws ProjetoException {

        conexao = ConnectionFactory.getConnection();

        try {

            Usuario usuarioSessao = (Usuario) SessaoUtil.resgatarDaSessao(USUARIO_SESSAO);

            PreparedStatement ps = conexao.prepareStatement(ALTERAR_USUARIO);
            ps.setString(1, usuario.getNome().toUpperCase());
            ps.setString(2, usuario.getLogin().toUpperCase());
            ps.setString(3, usuario.getSenha());
            ps.setInt(4, usuarioSessao.getId());

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

    public Boolean verificarSenhaUsuario(ParametrosVerificarSenhaUsuarioDTO parametrosVerificarSenhaUsuarioDTO) {

        boolean retorno = false;

        conexao = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conexao.prepareStatement(SELECT_ALTERAR_SENHA);
            ps.setInt(1, parametrosVerificarSenhaUsuarioDTO.getIdUsuario());
            ps.setString(2, parametrosVerificarSenhaUsuarioDTO.getSenhaAtual());
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
}