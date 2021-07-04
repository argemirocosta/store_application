package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioBuilder implements Builder {

    private Usuario usuario;

    public UsuarioBuilder() {
        this.usuario = new Usuario();
    }

    public UsuarioBuilder comId(Integer id) {
        usuario.setId(id);
        return this;
    }

    public UsuarioBuilder comNome(String nome){
        usuario.setNome(nome);
        return this;
    }

    public UsuarioBuilder comLogin(String login){
        usuario.setLogin(login);
        return this;
    }

    public UsuarioBuilder comSenha(String senha){
        usuario.setSenha(senha);
        return this;
    }

    public UsuarioBuilder comAtivo(Boolean ativo){
        usuario.setAtivo(ativo);
        return this;
    }

    public Usuario construir() {
        return this.usuario;
    }

    public Usuario mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comNome(rs.getString("nome"))
                .comLogin(rs.getString("login"))
                .comSenha(rs.getString("senha"))
                .comAtivo(rs.getBoolean("ativo"))
                .construir();
    }
}
