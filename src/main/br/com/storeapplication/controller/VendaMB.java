package br.com.storeapplication.controller;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.dto.VendasComDescontoDTO;
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
import br.com.storeapplication.service.RecebimentosCartaoService;
import br.com.storeapplication.service.VendaService;
import br.com.storeapplication.util.DataUtil;
import br.com.storeapplication.util.JSFUtil;

@ViewScoped
@ManagedBean
public class VendaMB {

    private Venda venda;
    private List<Venda> listaVendas;
    private BuscaRelatorio busca;
    private Double totalVendidoNoPeriodoSemDesconto;
    private Double mediaDiariaColecao;
    private Double mediaDiariaPromocao;
    private Double valorEstoque;
    private Double valorMercadoriaParaRepor;
    private Double valorCartaoPeriodo;
    private Double valorRecebidoCartao;
    private Double valorDescontoCartao;
    private VendaService vendaService;
    private FormaPagamentoService formaPagamentoService;
    private List<FormaPagamento> listaFormasPagamento;
    private RecebimentosCartaoService recebimentosCartaoService;
    private ArrayList<VendasComDescontoDTO> listaVendasComDesconto;

    public VendaMB() {
        venda = new Venda();
        listaVendas = new ArrayList<>();
        busca = new BuscaRelatorio();
        busca.setPeriodoinicial(DataUtil.retornarDataAtual());
        busca.setPeriodofinal(DataUtil.retornarDataAtual());
        totalVendidoNoPeriodoSemDesconto = 0.0;
        mediaDiariaColecao = 0.0;
        mediaDiariaPromocao = 0.0;
        valorEstoque = 0.0;
        valorMercadoriaParaRepor = 0.0;
        valorCartaoPeriodo = 0.0;
        valorRecebidoCartao = 0.0;
        valorDescontoCartao = 0.0;
        vendaService = new VendaService();
        recebimentosCartaoService = new RecebimentosCartaoService();
        formaPagamentoService = new FormaPagamentoService();
        listaFormasPagamento = new ArrayList<>();
        listaVendasComDesconto = new ArrayList<>();
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

    public void carregarVendasNoPeriodo(){
        totalVendidoPeriodoSemDesconto();
        totalVendidoPeriodoComDesconto();
    }

    private void totalVendidoPeriodoSemDesconto() {
        totalVendidoNoPeriodoSemDesconto = vendaService.consultarVendasPorPeriodoSemDesconto(busca);
    }

    private void totalVendidoPeriodoComDesconto() {
        listaVendasComDesconto = vendaService.consultarVendasPorPeriodoComDesconto(busca);
    }

    public void calcularMediaDiaria() {
        calcularMediaDiariaColecao();
        calcularMediaDiariaPromocao();
    }

    private void calcularMediaDiariaColecao() {
        mediaDiariaColecao = vendaService.consultarMediaDiariaColecao(busca);
    }

    private void calcularMediaDiariaPromocao() {
        mediaDiariaPromocao = vendaService.consultarMediaDiariaPromocao(busca);
    }

    public void calcularEstoque() {
        valorEstoque = vendaService.calcularEstoque();
    }

    public void calcularValorMercadoriaParaRepor() {
        valorMercadoriaParaRepor = vendaService.consultarValorMercadoriaParaRepor(busca);
    }

    public void calcularDescontoCartao() {
        DescontoCartaoDTO dto = recebimentosCartaoService.consultarDescontoCartao(busca);
        valorCartaoPeriodo = dto.getValorCartaoPeriodo();
        valorRecebidoCartao = dto.getValorRecebido();
        valorDescontoCartao = dto.getValorDescontoCartao();
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

    public Double getTotalVendidoNoPeriodoSemDesconto() {
        return totalVendidoNoPeriodoSemDesconto;
    }

    public void setTotalVendidoNoPeriodoSemDesconto(Double totalVendidoNoPeriodoSemDesconto) {
        this.totalVendidoNoPeriodoSemDesconto = totalVendidoNoPeriodoSemDesconto;
    }

    public Double getMediaDiariaColecao() {
        return mediaDiariaColecao;
    }

    public void setMediaDiariaColecao(Double mediaDiariaColecao) {
        this.mediaDiariaColecao = mediaDiariaColecao;
    }

    public Double getMediaDiariaPromocao() {
        return mediaDiariaPromocao;
    }

    public void setMediaDiariaPromocao(Double mediaDiariaPromocao) {
        this.mediaDiariaPromocao = mediaDiariaPromocao;
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

    public ArrayList<VendasComDescontoDTO> getListaVendasComDesconto() {
        return listaVendasComDesconto;
    }

    public void setListaVendasComDesconto(ArrayList<VendasComDescontoDTO> listaVendasComDesconto) {
        this.listaVendasComDesconto = listaVendasComDesconto;
    }

    public Double getValorCartaoPeriodo() {
        return valorCartaoPeriodo;
    }

    public void setValorCartaoPeriodo(Double valorCartaoPeriodo) {
        this.valorCartaoPeriodo = valorCartaoPeriodo;
    }

    public Double getValorRecebidoCartao() {
        return valorRecebidoCartao;
    }

    public void setValorRecebidoCartao(Double valorRecebidoCartao) {
        this.valorRecebidoCartao = valorRecebidoCartao;
    }

    public Double getValorDescontoCartao() {
        return valorDescontoCartao;
    }

    public void setValorDescontoCartao(Double valorDescontoCartao) {
        this.valorDescontoCartao = valorDescontoCartao;
    }
}