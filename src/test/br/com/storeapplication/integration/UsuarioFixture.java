package br.com.storeapplication.integration;

import br.com.storeapplication.dao.UsuarioDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.builder.UsuarioBuilder;

import java.util.UUID;

/**
 * Cria um usuário "fresco" no banco real (via UsuarioDAO) para isolar os dados
 * de cada teste de integração. Como as queries de Venda/Cliente são filtradas
 * por "usuario = ?", cada teste com seu próprio usuário não interfere nos dados
 * de outros testes - não é necessário rollback por teste.
 *
 * Nota: UsuarioDAO.login() já coloca o usuário retornado na sessão simulada
 * (via SessaoUtil.adicionarNaSessao), então criarUsuario() já deixa o usuário
 * "logado". Ainda assim, prefira chamar loginComoUsuario() explicitamente nos
 * testes para legibilidade e para trocar de usuário quando necessário.
 */
public final class UsuarioFixture {

    private UsuarioFixture() {
    }

    public static Usuario criarUsuario() throws ProjetoException {
        String loginUnico = "it_" + UUID.randomUUID().toString().substring(0, 8);

        Usuario novoUsuario = new UsuarioBuilder()
                .comNome("Usuario Integração")
                .comLogin(loginUnico)
                .comSenha("senha123")
                .construir();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.inserirUsuario(novoUsuario);

        Usuario credenciais = new UsuarioBuilder()
                .comLogin(loginUnico)
                .comSenha("senha123")
                .construir();

        return usuarioDAO.login(credenciais);
    }
}
