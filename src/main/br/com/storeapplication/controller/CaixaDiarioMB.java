package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.service.CaixaDiarioService;
import br.com.storeapplication.util.JSFUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static br.com.storeapplication.shared.Dialogs.DIALOG_CAIXA_DIARIO;
import static br.com.storeapplication.shared.Mensagens.*;

@ViewScoped
@ManagedBean
public class CaixaDiarioMB {

    private CaixaDiario caixaDiario;
    private CaixaDiarioService caixaDiarioService;

    public CaixaDiarioMB() {
        caixaDiario = new CaixaDiario();
        caixaDiarioService = new CaixaDiarioService();
    }

    public void inserirCaixaDiario() {
        try {
            caixaDiarioService.inserirCaixaDiario(caixaDiario);
            limparCampos();
            JSFUtil.fecharDialog(DIALOG_CAIXA_DIARIO);
            JSFUtil.adicionarMensagemSucesso(CAIXA_DIARIO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(CAIXA_DIARIO_ERRO, ERRO);
        }
    }

    private void limparCampos() {
        caixaDiario = new CaixaDiario();
    }

    public CaixaDiario getCaixaDiario() {
        return caixaDiario;
    }

    public void setCaixaDiario(CaixaDiario caixaDiario) {
        this.caixaDiario = caixaDiario;
    }
}
