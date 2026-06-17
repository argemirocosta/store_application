package br.com.storeapplication.service;

import br.com.storeapplication.dao.CaixaDiarioDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.CaixaDiario;

public class CaixaDiarioService {

    private CaixaDiarioDAO caixaDiarioDAO;

    public CaixaDiarioService() {
        caixaDiarioDAO = new CaixaDiarioDAO();
    }

    public void inserirCaixaDiario(CaixaDiario caixaDiario) throws ProjetoException {
        caixaDiarioDAO.inserirCaixaDiario(caixaDiario);
    }
}
