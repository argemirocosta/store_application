package br.com.storeapplication.service;

import br.com.storeapplication.dao.RecebimentosCartaoDAO;
import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.model.BuscaRelatorio;

public class RecebimentosCartaoService {

    private RecebimentosCartaoDAO recebimentosCartaoDAO;

    public RecebimentosCartaoService() {
        recebimentosCartaoDAO = new RecebimentosCartaoDAO();
    }

    public DescontoCartaoDTO consultarDescontoCartao(BuscaRelatorio busca) {
        return recebimentosCartaoDAO.consultarDescontoCartao(busca);
    }
}
