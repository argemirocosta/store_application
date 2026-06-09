package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {

    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private Boolean ativo;

    public Usuario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
                Objects.equals(nome, usuario.nome) &&
                Objects.equals(login, usuario.login) &&
                Objects.equals(senha, usuario.senha) &&
                Objects.equals(ativo, usuario.ativo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, login, senha, ativo);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", ativo=" + ativo +
                '}';
    }

}