package br.com.storeapplication.factory;

import br.com.storeapplication.util.AmbienteUtil;

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
            conexoes.setUrlBanco(AmbienteUtil.obterVariavelAmbiente("STORE_DB_URL_DEPLOY"));
            conexoes.setUsuario(AmbienteUtil.obterVariavelAmbiente("DB_USUARIO_DEPLOY"));
            conexoes.setSenha(AmbienteUtil.obterVariavelAmbiente("DB_SENHA_DEPLOY"));
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            conexoes.setUrlBanco(AmbienteUtil.obterVariavelAmbiente("STORE_DB_URL_PRODUCAO"));
            conexoes.setUsuario(AmbienteUtil.obterVariavelAmbiente("DB_USUARIO_PRODUCAO"));
            conexoes.setSenha(AmbienteUtil.obterVariavelAmbiente("DB_SENHA_PRODUCAO"));
        }

        return conexoes;
    }

}
