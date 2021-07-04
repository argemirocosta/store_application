package br.com.storeapplication.controller;

import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.service.FormaPagamentoService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean
public class FormaPagamentoMB {

    private FormaPagamento formaPagamento;
    private List<FormaPagamento> listaFormasPagamento;
    private FormaPagamentoService formaPagamentoService;

    public FormaPagamentoMB() {
        formaPagamento = new FormaPagamento();
        formaPagamentoService = new FormaPagamentoService();
    }

    public void listarFormasPagamento() {
        listaFormasPagamento = formaPagamentoService.listarFormasPagamento();
    }

}