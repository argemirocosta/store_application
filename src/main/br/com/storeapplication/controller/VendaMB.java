package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.Venda;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static br.com.storeapplication.shared.Dialogs.*;
import static br.com.storeapplication.shared.Mensagens.*;

import br.com.storeapplication.service.FormaPagamentoService;
import br.com.storeapplication.service.VendaService;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.JSFUtil;

@ViewScoped
@ManagedBean
public class VendaMB {

    private Venda venda;
    private List<Venda> listaVendas;
    private List<Venda> listaVendasPorCliente;
    private BuscaRelatorio busca;
    private Double totalVendidoNoPeriodo;
    private Double valorReceberGeral;
    private Double valorReceberGeralTotal;
    private VendaService vendaService;
    private FormaPagamentoService formaPagamentoService;
    private List<FormaPagamento> listaFormasPagamento;

    public VendaMB() {
        venda = new Venda();
        listaVendas = new ArrayList<>();
        listaVendasPorCliente = new ArrayList<>();
        busca = new BuscaRelatorio();
        busca.setPeriodoinicial(DataUtil.retornarDataAtual());
        busca.setPeriodofinal(DataUtil.retornarDataAtual());
        totalVendidoNoPeriodo = 0.0;
        vendaService = new VendaService();
        formaPagamentoService = new FormaPagamentoService();
        listaFormasPagamento = new ArrayList<>();
    }

    private void limparCampos() {
        venda.setQtd(null);
        venda.setValor(null);
    }

    public void inserirVenda() {
        try {
            vendaService.inserirVenda(venda);
            limparCampos();
            listarVendas();
            JSFUtil.adicionarMensagemSucesso(VENDA_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(VENDA_ERRO, ERRO);
        }
    }

    public void totalVendidoPeriodo() {
        totalVendidoNoPeriodo = vendaService.consultarVendasPorPeriodo(busca);
    }

    private void somarGeralTotal() {
        totalVendidoNoPeriodo = vendaService.calcularVendasTotal();
        valorReceberGeralTotal = totalVendidoNoPeriodo - valorReceberGeral;
    }

    public void listarRankingDosClientes() {
        listaVendasPorCliente = vendaService.listarRankingDosClientes();
    }

    private void listarFormasPagamento() {
        listaFormasPagamento = formaPagamentoService.listarFormasPagamento();
    }

    public void abrirDialogVender() {
        listarVendas();
        listarFormasPagamento();
        JSFUtil.abrirDialog("dlgVender");
    }

    public void listarVendas() {
        listaVendas = vendaService.listarVendas(venda);
    }

    public void cancelarVenda() {

        try {
            vendaService.cancelarVenda(venda.getId());
            listarVendas();
            JSFUtil.fecharDialog(DIALOG_CANCELAR_VENDA);
            JSFUtil.adicionarMensagemSucesso(VENDA_CANCELADA_SUCESSO, SUCESSO);
        } catch (Exception ex) {
            JSFUtil.adicionarMensagemErro(VENDA_CANCELADA_ERRO, ERRO);
        }


    }

    //GETTERS E SETTERS

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public List<Venda> getListaVendas() {
        return listaVendas;
    }

    public void setListaVendas(List<Venda> listaVendas) {
        this.listaVendas = listaVendas;
    }

    public List<Venda> getListaVendasPorCliente() {
        return listaVendasPorCliente;
    }

    public void setListaVendasPorCliente(List<Venda> listaVendasPorCliente) {
        this.listaVendasPorCliente = listaVendasPorCliente;
    }

    public BuscaRelatorio getBusca() {
        return busca;
    }

    public void setBusca(BuscaRelatorio busca) {
        this.busca = busca;
    }

    public Double getValorReceberGeral() {
        return valorReceberGeral;
    }

    public void setValorReceberGeral(Double valorReceberGeral) {
        this.valorReceberGeral = valorReceberGeral;
    }

    public Double getValorReceberGeralTotal() {
        return valorReceberGeralTotal;
    }

    public void setValorReceberGeralTotal(Double valorReceberGeralTotal) {
        this.valorReceberGeralTotal = valorReceberGeralTotal;
    }

    public Double getTotalVendidoNoPeriodo() {
        return totalVendidoNoPeriodo;
    }

    public void setTotalVendidoNoPeriodo(Double totalVendidoNoPeriodo) {
        this.totalVendidoNoPeriodo = totalVendidoNoPeriodo;
    }

    public List<FormaPagamento> getListaFormasPagamento() {
        return listaFormasPagamento;
    }

    public void setListaFormasPagamento(List<FormaPagamento> listaFormasPagamento) {
        this.listaFormasPagamento = listaFormasPagamento;
    }
}