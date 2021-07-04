package br.com.storeapplication.shared.queries;

public class UsuarioDAOQueries {

    private UsuarioDAOQueries() {
    }

    public static final String SELECT_LOGIN = "SELECT id, nome, login, senha, ativo FROM vendas.usuario WHERE login = ? AND senha = ?";

    public static final String SELECT_ALTERAR_SENHA = "SELECT id FROM vendas.usuario WHERE id = ? AND senha = ?";

    public static final String INSERIR_USUARIO = "INSERT INTO vendas.usuario (nome, login, senha, ativo) VALUES (?,?,?,TRUE)";

    public static final String ALTERAR_USUARIO = "UPDATE vendas.usuario SET nome = ?, login = ?, senha = ? WHERE id = ?";

}
