package br.com.storeapplication.util;


public final class IntegerUtil {

    public static Integer tratarValorVindoZero(String valor) {

        Integer retorno = null;

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(valor)){
            retorno = Integer.parseInt(valor);
        }

        return retorno;
    }

}
