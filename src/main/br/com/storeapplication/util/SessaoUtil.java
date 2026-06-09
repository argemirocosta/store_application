package br.com.storeapplication.util;

import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.shared.Sessao;
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

    public static Usuario resgatarUsuarioDaSessao() {
        return (Usuario) resgatarDaSessao(Sessao.USUARIO_SESSAO);
    }

}