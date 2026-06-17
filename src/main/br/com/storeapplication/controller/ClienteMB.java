package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Cliente;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.storeapplication.service.ClienteService;
import br.com.storeapplication.util.JSFUtil;
import br.com.storeapplication.util.SessaoUtil;

import static br.com.storeapplication.shared.Dialogs.*;
import static br.com.storeapplication.shared.Mensagens.*;

@ViewScoped
@ManagedBean
public class ClienteMB {

    private Cliente cliente;
    private List<Cliente> listaClientes;
    private String campoBusca;
    private ClienteService clienteService;

    public ClienteMB() {
        cliente = new Cliente();
        clienteService = new ClienteService();
    }

    public void limparBusca() {
        listaClientes = null;
        campoBusca = null;
        listarClientes();
    }

    public void limparCampos() {
        cliente = new Cliente();
    }

    public void buscarClientePorNome() {
        listaClientes = clienteService.buscarClientePorNome(campoBusca);
    }

    public void buscarClientePorId() {
        Integer idCliente = (Integer) SessaoUtil.resgatarDaSessao("clienteCriado");
        listaClientes = clienteService.buscarClientePorId(idCliente);
    }

    public Boolean verificarClienteEhCadastrado(String telefone) {
        return clienteService.verificarClienteEhCadastrado(telefone);
    }

    public void inserirCliente() {
        if(!verificarClienteEhCadastrado(cliente.getTelefone1())) {
            try {
                clienteService.inserirCliente(cliente);
                limparCampos();
                buscarClientePorId();
                SessaoUtil.retirarDaSessao("clienteCriado");
                JSFUtil.fecharDialog(DIALOG_CADASTRAR_CLIENTE);
                JSFUtil.adicionarMensagemSucesso(CLIENTE_CADASTRADO_SUCESSO, SUCESSO);
            } catch (ProjetoException e) {
                JSFUtil.adicionarMensagemErro(CLIENTE_CADASTRADO_ERRO, ERRO);
            }
        }
        else{
            JSFUtil.adicionarMensagemErro(CLIENTE_JA_CADASTRADO_ERRO, ERRO);
        }
    }

    public void alterarCliente() {
        try {
            clienteService.alterarCliente(cliente);
            limparCampos();
            listarClientes();
            JSFUtil.fecharDialog(DIALOG_ALTERAR_CLIENTE);
            JSFUtil.adicionarMensagemSucesso(CLIENTE_ALTERADO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(CLIENTE_ALTERADO_ERRO, ERRO);
        }
    }

    public void deletarCliente() {
        try {
            clienteService.deletarCliente(cliente);
            listarClientes();
            JSFUtil.fecharDialog(DIALOG_DELETAR_CLIENTE);
            JSFUtil.adicionarMensagemSucesso(CLIENTE_EXCLUIDO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(CLIENTE_EXCLUIDO_ERRO, ERRO);
        }
    }

    public void listarClientes() {
        listaClientes = clienteService.listarClientes();
    }

    //GETTERS E SETTERS

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getCampoBusca() {
        return campoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }

}