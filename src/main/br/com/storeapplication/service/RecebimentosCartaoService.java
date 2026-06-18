package br.com.storeapplication.service;

import br.com.storeapplication.dao.RecebimentosCartaoDAO;
import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.dto.RecebimentoCartaoRelatorioDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.RecebimentoCartao;

import java.util.ArrayList;

public class RecebimentosCartaoService {

    private RecebimentosCartaoDAO recebimentosCartaoDAO;

    public RecebimentosCartaoService() {
        recebimentosCartaoDAO = new RecebimentosCartaoDAO();
    }

    public void inserirRecebimentoCartao(RecebimentoCartao recebimento) throws ProjetoException {
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimento);
    }

    public ArrayList<RecebimentoCartaoRelatorioDTO> consultarRecebimentosCartao(BuscaRelatorio busca) {
        return recebimentosCartaoDAO.consultarRecebimentosCartao(busca);
    }

    public DescontoCartaoDTO consultarDescontoCartao(BuscaRelatorio busca) {
        return recebimentosCartaoDAO.consultarDescontoCartao(busca);
    }
}
