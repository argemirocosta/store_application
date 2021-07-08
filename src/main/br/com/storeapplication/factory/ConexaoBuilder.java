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
            conexoes.setUrlBanco("***REMOVED***");
            conexoes.setUsuario("***REMOVED***");
            conexoes.setSenha("***REMOVED***");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            conexoes.setUrlBanco("***REMOVED***");
            conexoes.setUsuario("***REMOVED***");
            conexoes.setSenha("***REMOVED***");
        }

        return conexoes;
    }

}
