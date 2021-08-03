package br.com.storeapplication.service;

import br.com.storeapplication.dao.VendaDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Venda;

import java.util.List;

public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();

    public VendaService() {
    }

    public void inserirVenda(Venda venda) throws ProjetoException {
        vendaDAO.inserirVenda(venda);
    }

    public Double consultarVendasPorPeriodo(BuscaRelatorio busca) {
        return vendaDAO.consultarVendasPorPeriodo(busca);
    }

    public Double consultarMediaDiaria(BuscaRelatorio busca) {
        return vendaDAO.consultarMediaDiaria(busca);
    }

    public Double calcularEstoque() {
        return vendaDAO.calcularEstoque();
    }

    public List<Venda> listarVendas(Venda venda) {
        return vendaDAO.listarVendas(venda);
    }

    public void cancelarVenda(Integer codVenda) {
        vendaDAO.cancelarVenda(codVenda);
    }

    public Double consultarValorMercadoriaParaRepor(BuscaRelatorio busca) {
        return vendaDAO.consultarValorMercadoriaParaRepor(busca);
    }
}