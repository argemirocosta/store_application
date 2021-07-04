package br.com.storeapplication.util;

import javax.faces.context.FacesContext;

public class SessaoUtil {

    public static void adicionarNaSessao(Object objeto, String nomeObjetoSessao) {
        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put(nomeObjetoSessao, objeto);
    }

    public static Object resgatarDaSessao(String nomeObjetoSessao) {

        return FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get(nomeObjetoSessao);
    }

    public static void retirarDaSessao(String nomeObjetoSessao) {

        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().remove(nomeObjetoSessao);
    }

}