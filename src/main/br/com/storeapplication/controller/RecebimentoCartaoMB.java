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
            JSFUtil.adicionarMensagemSucesso(RECEBIMENTO_CARTAO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(RECEBIMENTO_CARTAO_ERRO, ERRO);
        }
    }

    private void limparCampos() {
        recebimentoCartao = new RecebimentoCartao();
    }

    public RecebimentoCartao getRecebimentoCartao() {
        return recebimentoCartao;
    }

    public void setRecebimentoCartao(RecebimentoCartao recebimentoCartao) {
        this.recebimentoCartao = recebimentoCartao;
    }
}
