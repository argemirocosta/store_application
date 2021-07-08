package br.com.storeapplication.factory;


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
            conexoes.setUrlBanco("jdbc:postgresql://10.100.33.197:5432/store_application");
            conexoes.setUsuario("webadmin");
            conexoes.setSenha("MKOpat83336");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            conexoes.setUrlBanco("jdbc:postgresql://node73163-env-7907985.jelastic.saveincloud.net:11965/store_application");
            conexoes.setUsuario("webadmin");
            conexoes.setSenha("MKOpat83336");
        }

        return conexoes;
    }

}
