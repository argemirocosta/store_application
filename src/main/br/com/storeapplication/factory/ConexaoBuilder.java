package br.com.storeapplication.factory;

import java.net.URI;
import java.net.URISyntaxException;

public class ConexaoBuilder {

    public static Conexoes carregarDadosConexao() throws URISyntaxException {

        Propriedades propriedades = new Propriedades();

        Conexoes conexoes = new Conexoes();
        if(propriedades.Conexao.equals(Propriedades.Conexoes.LOCALHOST)){
            conexoes.setUrlBanco("jdbc:postgresql://localhost:5432/store_application");
            conexoes.setUsuario("postgres");
            conexoes.setSenha("post");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            URI dbUri = new URI(System.getenv("DATABASE_URL"));
            conexoes.setUrlBanco("jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require");
            conexoes.setUsuario(dbUri.getUserInfo().split(":")[0]);
            conexoes.setSenha(dbUri.getUserInfo().split(":")[1]);
        }

        return conexoes;
    }

}
