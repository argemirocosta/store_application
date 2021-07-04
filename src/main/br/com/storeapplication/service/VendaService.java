package br.com.storeapplication.service;

import br.com.storeapplication.dao.VendaDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Pagamento;
import br.com.storeapplication.model.Venda;

import java.util.List;

public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();

    public VendaService() {
    }

    public void inserirVenda(Venda venda) throws ProjetoException {
        vendaDAO.inserirVenda(venda);
    }

    public Integer verificarSemPagamentos(Integer codVenda) {
        return vendaDAO.verificarSemPagamentos(codVenda);
    }

    public Double calcularValorEmAberto(Integer codVenda) {
        return vendaDAO.calcularValorEmAberto(codVenda);
    }

    public void inserirPagamento(Venda venda, Pagamento pagamento) throws ProjetoException {
        vendaDAO.inserirPagamento(venda, pagamento);
    }

    public Double consultarVendasPorPeriodo(BuscaRelatorio busca) {
        return vendaDAO.consultarVendasPorPeriodo(busca);
    }

    public Double calcularVendasTotal() {
        return vendaDAO.calcularVendasTotal();
    }

    public Double calcularValorReceberGeral() {
        return vendaDAO.calcularValorReceberGeral();
    }

    public List<Venda> listarRankingDosClientes() {
        return vendaDAO.listarVendasPorCliente();
    }

    public List<Pagamento> listarPagamentos(Integer codVenda) {
        return vendaDAO.listarPagamentos(codVenda);
    }

    public List<Venda> listarValorAReceberPorPessoa() {
        return vendaDAO.listarValorAReceber();
    }

    public List<Venda> listarVendas(Venda venda) {
        return vendaDAO.listarVendas(venda);
    }

    public void cancelarVenda(Integer codVenda) {
        vendaDAO.cancelarVenda(codVenda);
    }

    public void cancelarPagamento(Integer codPagamento) {
        vendaDAO.cancelarPagamento(codPagamento);
    }

    public Boolean verificarSeExistePagamentoParaVenda(Integer codVenda) {
        return vendaDAO.verificarSeExistePagamentoParaVenda(codVenda);
    }

    public Double calcularValorEmAbertoDaVenda(Integer codVenda) {
        return vendaDAO.calcularValorEmAberto(codVenda);
    }
}