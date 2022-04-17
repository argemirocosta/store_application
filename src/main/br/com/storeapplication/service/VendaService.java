package br.com.storeapplication.service;

import br.com.storeapplication.dao.VendaDAO;
import br.com.storeapplication.dto.VendasComDescontoDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Venda;

import java.util.ArrayList;
import java.util.List;

public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();

    public VendaService() {
    }

    public void inserirVenda(Venda venda) throws ProjetoException {
        vendaDAO.inserirVenda(venda);
    }

    public Double consultarVendasPorPeriodoSemDesconto(BuscaRelatorio busca) {
        return vendaDAO.consultarVendasPorPeriodoSemDesconto(busca);
    }

    public ArrayList<VendasComDescontoDTO> consultarVendasPorPeriodoComDesconto(BuscaRelatorio busca) {
        return vendaDAO.consultarVendasPorPeriodoComDesconto(busca);
    }

    public Double consultarMediaDiariaColecao(BuscaRelatorio busca) {
        return vendaDAO.consultarMediaDiariaColecao(busca);
    }

    public Double consultarMediaDiariaPromocao(BuscaRelatorio busca) {
        return vendaDAO.consultarMediaDiariaPromocao(busca);
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