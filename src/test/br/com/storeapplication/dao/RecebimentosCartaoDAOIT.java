package br.com.storeapplication.dao;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
import br.com.storeapplication.integration.RecebimentosCartaoFixture;
import br.com.storeapplication.integration.UsuarioFixture;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.Venda;
import br.com.storeapplication.model.builder.ClienteBuilder;
import br.com.storeapplication.model.builder.FormaPagamentoBuilder;
import br.com.storeapplication.model.builder.VendaBuilder;
import br.com.storeapplication.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecebimentosCartaoDAOIT extends PostgresIntegrationTestBase {

    private static final FormaPagamento CARTAO_CREDITO =
            new FormaPagamentoBuilder().comId(2).comDescricao("CARTÃO CRÉDITO").construir();
    private static final FormaPagamento CARTAO_DEBITO =
            new FormaPagamentoBuilder().comId(4).comDescricao("CARTÃO DÉBITO").construir();

    private final RecebimentosCartaoDAO recebimentosCartaoDAO = new RecebimentosCartaoDAO();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    private Usuario usuario;

    @BeforeEach
    void setUp() throws ProjetoException {
        usuario = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuario);
    }

    @Test
    void deveCalcularDescontoCartaoNoPeriodo() throws ProjetoException, SQLException {
        Cliente cliente = criarCliente("Cliente Cartão");
        Date hoje = DataUtil.retornarDataAtual();

        inserirVenda(cliente, 100.0, hoje, CARTAO_CREDITO);
        inserirVenda(cliente, 50.0, hoje, CARTAO_DEBITO);

        RecebimentosCartaoFixture.inserirRecebimentoCartao(hoje, 140.0);

        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(hoje, -1));
        busca.setPeriodofinal(adicionarDias(hoje, 1));

        DescontoCartaoDTO dto = recebimentosCartaoDAO.consultarDescontoCartao(busca);

        assertNotNull(dto);
        assertEquals(150.0, dto.getValorCartaoPeriodo(), 0.01);
        assertEquals(140.0, dto.getValorRecebido(), 0.01);
        assertEquals(10.0, dto.getValorDescontoCartao(), 0.01);
    }

    @Test
    void deveRetornarZerosQuandoNaoHaRecebimentosNoPeriodo() {
        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(DataUtil.retornarDataAtual(), -90));
        busca.setPeriodofinal(adicionarDias(DataUtil.retornarDataAtual(), -60));

        DescontoCartaoDTO dto = recebimentosCartaoDAO.consultarDescontoCartao(busca);

        assertNotNull(dto);
        assertEquals(0.0, dto.getValorCartaoPeriodo(), 0.01);
        assertEquals(0.0, dto.getValorRecebido(), 0.01);
        assertEquals(0.0, dto.getValorDescontoCartao(), 0.01);
    }

    private Cliente criarCliente(String nome) throws ProjetoException {
        Cliente cliente = new ClienteBuilder()
                .comNome(nome)
                .comTelefone1("11999990000")
                .construir();

        clienteDAO.inserirCliente(cliente);

        return clienteDAO.listarClientes().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cliente recém-criado não encontrado"));
    }

    private void inserirVenda(Cliente cliente, double valor, Date data, FormaPagamento formaPagamento) throws ProjetoException {
        Venda venda = new VendaBuilder()
                .comCliente(cliente)
                .comValor(valor)
                .comQtd(1)
                .comData(data)
                .comFormaPagamento(formaPagamento)
                .comDesconto(false)
                .comPercentualDesconto(0.0)
                .construir();

        vendaDAO.inserirVenda(venda);
    }

    private Date adicionarDias(Date data, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }
}
