package br.com.storeapplication.factory;

public class Propriedades {

    public static Conexoes Conexao = Conexoes.PRODUCAO;

    public enum Conexoes {
        LOCALHOST,
        DEPLOY,
        PRODUCAO;
    }




}
