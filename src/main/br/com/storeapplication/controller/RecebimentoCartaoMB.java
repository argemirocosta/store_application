package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.service.RecebimentosCartaoService;
import br.com.storeapplication.util.JSFUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static br.com.storeapplication.shared.Mensagens.*;

@ViewScoped
@ManagedBean
public class RecebimentoCartaoMB {

    private RecebimentoCartao recebimentoCartao;
    private RecebimentosCartaoService recebimentosCartaoService;

    public RecebimentoCartaoMB() {
        recebimentoCartao = new RecebimentoCartao();
        recebimentosCartaoService = new RecebimentosCartaoService();
    }

    public void inserirRecebimentoCartao() {
        try {
            recebimentosCartaoService.inserirRecebimentoCartao(recebimentoCartao);
            limparCampos();
            // dialog permanece aberto intencionalmente para permitir múltiplas inserções consecutivas
            JSFUtil.adicionarMensagemSucesso(RECEBIMENTO_CARTAO_ADICIONADO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(RECEBIMENTO_CARTAO_ADICIONADO_ERRO, ERRO);
        }
    }

    private void limparCampos() {
        recebimentoCartao = new RecebimentoCartao();
    }

    //GETTERS E SETTERS

    public RecebimentoCartao getRecebimentoCartao() {
        return recebimentoCartao;
    }

    public void setRecebimentoCartao(RecebimentoCartao recebimentoCartao) {
        this.recebimentoCartao = recebimentoCartao;
    }
}
