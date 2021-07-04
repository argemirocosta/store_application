package br.com.storeapplication.util;

public final class VerificadorUtil {

    public static Boolean verificarSeObjetoNulo(Object object) {
        Boolean retorno = true;

        if (object != null) {
            retorno = false;
        }

        return retorno;
    }

    public static Boolean verificarSeObjetoNuloOuVazio(Object object) {
        Boolean retorno = true;

        if(object != null && !object.equals("")){
            retorno = false;
        }

        return retorno;
    }

}
