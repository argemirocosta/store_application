package br.com.storeapplication.util;


public final class StringUtil {

    public static String retirarCaracteresEspeciais(String textoParaSerTratado) {

        return textoParaSerTratado.replaceAll("[^0-9]", "");
    }

}
