package br.com.storeapplication.service;

import br.com.storeapplication.dao.EstoqueDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Estoque;

public class EstoqueService {

    private EstoqueDAO estoqueDAO;

    public EstoqueService() {
        estoqueDAO = new EstoqueDAO();
    }

    public void inserirEstoque(Estoque estoque) throws ProjetoException {
        estoqueDAO.inserirEstoque(estoque);
    }
}
