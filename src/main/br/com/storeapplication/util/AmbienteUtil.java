package br.com.storeapplication.util;

public final class AmbienteUtil {

    public static String obterVariavelAmbiente(String nome) {
        String valor = System.getProperty(nome);

        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(valor)){
            valor = System.getenv(nome);
        }

        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(valor)){
            throw new IllegalStateException("Variável de ambiente " + nome + " não definida.");
        }

        return valor;
    }

}
