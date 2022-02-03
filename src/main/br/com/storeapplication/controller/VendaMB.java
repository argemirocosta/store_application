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
    private BuscaRelatorio busca;
    private Double totalVendidoNoPeriodo;
    private Double mediaDiaria;
    private Double valorEstoque;
    private Double valorMercadoriaParaRepor;
    private VendaService vendaService;
    private FormaPagamentoService formaPagamentoService;
    private List<FormaPagamento> listaFormasPagamento;

    public VendaMB() {
        venda = new Venda();
        listaVendas = new ArrayList<>();
        busca = new BuscaRelatorio();
        busca.setPeriodoinicial(DataUtil.retornarDataAtual());
        busca.setPeriodofinal(DataUtil.retornarDataAtual());
        totalVendidoNoPeriodo = 0.0;
        mediaDiaria = 0.0;
        valorEstoque = 0.0;
        valorMercadoriaParaRepor = 0.0;
        vendaService = new VendaService();
        formaPagamentoService = new FormaPagamentoService();
        listaFormasPagamento = new ArrayList<>();
    }

    private void limparCampos() {
        venda.setQtd(1);
        venda.setValor(null);
        venda.setDesconto(false);
        venda.setPercentualDesconto(0.0);
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

    public void calcularMediaDiaria() {
        mediaDiaria = vendaService.consultarMediaDiaria(busca);
    }

    public void calcularEstoque() {
        valorEstoque = vendaService.calcularEstoque();
    }

    public void calcularValorMercadoriaParaRepor() {
        valorMercadoriaParaRepor = vendaService.consultarValorMercadoriaParaRepor(busca);
    }

    private void listarFormasPagamento() {
        listaFormasPagamento = formaPagamentoService.listarFormasPagamento();
    }

    public void abrirDialogVender() {
        listarVendas();
        listarFormasPagamento();
        JSFUtil.abrirDialog(DIALOG_VENDER);
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

    public BuscaRelatorio getBusca() {
        return busca;
    }

    public void setBusca(BuscaRelatorio busca) {
        this.busca = busca;
    }

    public Double getTotalVendidoNoPeriodo() {
        return totalVendidoNoPeriodo;
    }

    public void setTotalVendidoNoPeriodo(Double totalVendidoNoPeriodo) {
        this.totalVendidoNoPeriodo = totalVendidoNoPeriodo;
    }

    public Double getMediaDiaria() {
        return mediaDiaria;
    }

    public void setMediaDiaria(Double mediaDiaria) {
        this.mediaDiaria = mediaDiaria;
    }

    public List<FormaPagamento> getListaFormasPagamento() {
        return listaFormasPagamento;
    }

    public void setListaFormasPagamento(List<FormaPagamento> listaFormasPagamento) {
        this.listaFormasPagamento = listaFormasPagamento;
    }

    public Double getValorEstoque() {
        return valorEstoque;
    }

    public void setValorEstoque(Double valorEstoque) {
        this.valorEstoque = valorEstoque;
    }

    public Double getValorMercadoriaParaRepor() {
        return valorMercadoriaParaRepor;
    }

    public void setValorMercadoriaParaRepor(Double valorMercadoriaParaRepor) {
        this.valorMercadoriaParaRepor = valorMercadoriaParaRepor;
    }
}