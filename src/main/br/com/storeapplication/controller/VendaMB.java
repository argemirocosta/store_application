package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.Pagamento;
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
    private Pagamento pagamento;
    private List<Pagamento> listaPagamentos;
    private List<Venda> listaVendasPorCliente;
    private List<Venda> listaVendasEmAberto;
    private BuscaRelatorio busca;
    private Double totalVendidoNoPeriodo;
    private Double valorReceberGeral;
    private Double valorReceberGeralTotal;
    private Double valorEmAbertoDaVenda;
    private VendaService vendaService;
    private FormaPagamentoService formaPagamentoService;
    private List<FormaPagamento> listaFormasPagamento;

    public VendaMB() {
        venda = new Venda();
        pagamento = new Pagamento();
        listaVendas = new ArrayList<>();
        listaVendasPorCliente = new ArrayList<>();
        busca = new BuscaRelatorio();
        busca.setPeriodoinicial(DataUtil.retornarDataAtual());
        busca.setPeriodofinal(DataUtil.retornarDataAtual());
        totalVendidoNoPeriodo = 0.0;
        listaVendasEmAberto = new ArrayList<>();
        valorEmAbertoDaVenda = 0.0;
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
            JSFUtil.fecharDialog(DIALOG_VENDER);
            JSFUtil.adicionarMensagemSucesso(VENDA_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(VENDA_ERRO, ERRO);
        }
    }

    public void verificarParaInserirPagamento() {
        int pagamentoVenda = vendaService.verificarSemPagamentos(venda.getId());

        Double valorEmAberto = vendaService.calcularValorEmAbertoDaVenda(venda.getId());

        if (pagamento.getValor() > valorEmAberto) {
            JSFUtil.adicionarMensagemAdvertencia(PAGAMENTO_MAIOR_QUE_VENDA, AVISO);
        } else if (valorEmAberto > 0 || pagamentoVenda == 0) {
            inserirPagamento();

        } else {
            JSFUtil.adicionarMensagemAdvertencia(VENDA_JA_PAGA, AVISO);

        }

    }

    private void inserirPagamento() {
        try {
            vendaService.inserirPagamento(venda, pagamento);
            listarPagamentos();
            listarVendas();
            limparCampos();
            JSFUtil.adicionarMensagemSucesso(PAGAMENTO_SUCESSO, SUCESSO);
            calcularValorEmAbertoDaVenda();
            pagamento.setValor(null);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(PAGAMENTO_ERRO, ERRO);
        }
    }

    public void totalVendidoPeriodo() {
        totalVendidoNoPeriodo = vendaService.consultarVendasPorPeriodo(busca);
    }

    private void somarGeralTotal() {
        totalVendidoNoPeriodo = vendaService.calcularVendasTotal();
        valorReceberGeralTotal = totalVendidoNoPeriodo - valorReceberGeral;
    }

    public void calcularRecebidoGeral() {
        valorReceberGeral = vendaService.calcularValorReceberGeral();
        somarGeralTotal();
    }

    public void listarRankingDosClientes() {
        listaVendasPorCliente = vendaService.listarRankingDosClientes();
    }

    public void abrirTelaDePagamento() {
        listarPagamentos();
        calcularValorEmAbertoDaVenda();
        JSFUtil.abrirDialog("dlgPagamentos");
    }

    private void listarPagamentos() {
        listaPagamentos = vendaService.listarPagamentos(venda.getId());
    }

    private void listarFormasPagamento() {
        listaFormasPagamento = formaPagamentoService.listarFormasPagamento();
    }

    public void listarValorAReceberPorPessoa() {
        listaVendasEmAberto = vendaService.listarValorAReceberPorPessoa();
    }

    public void abrirDialogVender(){
        listarVendas();
        listarFormasPagamento();
        JSFUtil.abrirDialog("dlgVender");
    }

    public void listarVendas() {
        listaVendas = vendaService.listarVendas(venda);
    }

    public void cancelarVenda() {

        if (verificarSeCancelamentoPodeSerRealizado()) {
            try {
                vendaService.cancelarVenda(venda.getId());
                listarVendas();
                JSFUtil.fecharDialog(DIALOG_CANCELAR_VENDA);
                JSFUtil.adicionarMensagemSucesso(VENDA_CANCELADA_SUCESSO, SUCESSO);
            } catch (Exception ex) {
                JSFUtil.adicionarMensagemErro(VENDA_CANCELADA_ERRO, ERRO);
            }
        }

    }

    public void cancelarPagamento() {
        try {
            vendaService.cancelarPagamento(pagamento.getId());
            listarVendas();
            calcularValorEmAbertoDaVenda();
            JSFUtil.fecharDialog(DIALOG_CANCELAR_PAGAMENTO);
            JSFUtil.adicionarMensagemSucesso(PAGAMENTO_CANCELADO_SUCESSO, SUCESSO);
            listarPagamentos();
        } catch (Exception ex) {
            JSFUtil.adicionarMensagemErro(PAGAMENTO_CANCELADO_ERRO, ERRO);
        }
    }

    private Boolean verificarSeExistePagamentoParaVenda() {
        return vendaService.verificarSeExistePagamentoParaVenda(venda.getId());
    }

    private Boolean verificarSeCancelamentoPodeSerRealizado() {
        boolean retorno = true;

        if (venda.getSituacao().equals("PAGO")) {
            JSFUtil.adicionarMensagemAdvertencia(VENDA_JA_PAGA, AVISO);
            JSFUtil.fecharDialog(DIALOG_CANCELAR_VENDA);
            retorno = false;
        } else if (verificarSeExistePagamentoParaVenda()) {
            JSFUtil.adicionarMensagemAdvertencia(VENDA_JA_TEM_PAGAMENTO, AVISO);
            JSFUtil.fecharDialog(DIALOG_CANCELAR_VENDA);
            retorno = false;
        }

        return retorno;
    }

    private void calcularValorEmAbertoDaVenda() {
        valorEmAbertoDaVenda = vendaService.calcularValorEmAberto(venda.getId());
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

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public List<Pagamento> getListaPagamentos() {
        return listaPagamentos;
    }

    public void setListaPagamentos(List<Pagamento> listaPagamentos) {
        this.listaPagamentos = listaPagamentos;
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

    public List<Venda> getListaVendasEmAberto() {
        return listaVendasEmAberto;
    }

    public void setListaVendasEmAberto(List<Venda> listaVendasEmAberto) {
        this.listaVendasEmAberto = listaVendasEmAberto;
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

    public Double getValorEmAbertoDaVenda() {
        return valorEmAbertoDaVenda;
    }

    public void setValorEmAbertoDaVenda(Double valorEmAbertoDaVenda) {
        this.valorEmAbertoDaVenda = valorEmAbertoDaVenda;
    }

    public List<FormaPagamento> getListaFormasPagamento() {
        return listaFormasPagamento;
    }

    public void setListaFormasPagamento(List<FormaPagamento> listaFormasPagamento) {
        this.listaFormasPagamento = listaFormasPagamento;
    }
}