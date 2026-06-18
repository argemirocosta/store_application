package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.service.EstoqueService;
import br.com.storeapplication.util.JSFUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static br.com.storeapplication.shared.Mensagens.*;

@ViewScoped
@ManagedBean
public class EstoqueMB {

    private Estoque estoque;
    private EstoqueService estoqueService;

    public EstoqueMB() {
        estoque = new Estoque();
        estoqueService = new EstoqueService();
    }

    public void inserirEstoque() {
        try {
            estoqueService.inserirEstoque(estoque);
            limparCampos();
            // dialog permanece aberto intencionalmente para permitir múltiplas inserções consecutivas
            JSFUtil.adicionarMensagemSucesso(ESTOQUE_INSERIDO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(ESTOQUE_INSERIDO_ERRO, ERRO);
        }
    }

    private void limparCampos() {
        estoque = new Estoque();
    }

    //GETTERS E SETTERS

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }
}
