package br.com.storeapplication.factory;

import br.com.storeapplication.util.VerificadorUtil;

public class ConexaoBuilder {

    public static Conexoes carregarDadosConexao() {

        Propriedades propriedades = new Propriedades();

        Conexoes conexoes = new Conexoes();
        if(propriedades.Conexao.equals(Propriedades.Conexoes.LOCALHOST)){
            conexoes.setUrlBanco("jdbc:postgresql://localhost:5432/store_application");
            conexoes.setUsuario("postgres");
            conexoes.setSenha("post");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.DEPLOY)){
            conexoes.setUrlBanco(obterVariavelAmbiente("DB_URL_DEPLOY"));
            conexoes.setUsuario(obterVariavelAmbiente("DB_USUARIO_DEPLOY"));
            conexoes.setSenha(obterVariavelAmbiente("DB_SENHA_DEPLOY"));
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            conexoes.setUrlBanco(obterVariavelAmbiente("DB_URL_PRODUCAO"));
            conexoes.setUsuario(obterVariavelAmbiente("DB_USUARIO_PRODUCAO"));
            conexoes.setSenha(obterVariavelAmbiente("DB_SENHA_PRODUCAO"));
        }

        return conexoes;
    }

    private static String obterVariavelAmbiente(String nome) {
        String valor = System.getenv(nome);

        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(valor)){
            throw new IllegalStateException("Variável de ambiente " + nome + " não definida.");
        }

        return valor;
    }

}
