package br.com.storeapplication.service;

import br.com.storeapplication.dao.FormaPagamentoDAO;
import br.com.storeapplication.model.FormaPagamento;

import java.util.List;

public class FormaPagamentoService {

    private FormaPagamentoDAO formaPagamentoDAO;

    public FormaPagamentoService() {
        formaPagamentoDAO = new FormaPagamentoDAO();
    }

    public List<FormaPagamento> listarFormasPagamento() {
        return formaPagamentoDAO.listarFormasPagamento();
    }

}