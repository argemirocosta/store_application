package br.com.storeapplication.dao;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.dto.RecebimentoCartaoRelatorioDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.PostgresContainerSupport;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
import br.com.storeapplication.integration.RecebimentosCartaoFixture;
import br.com.storeapplication.integration.UsuarioFixture;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.Venda;
import br.com.storeapplication.model.builder.ClienteBuilder;
import br.com.storeapplication.model.builder.FormaPagamentoBuilder;
import br.com.storeapplication.model.builder.RecebimentoCartaoBuilder;
import br.com.storeapplication.model.builder.VendaBuilder;
import br.com.storeapplication.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void deveInserirRecebimentoCartao() throws ProjetoException, SQLException {
        RecebimentoCartao recebimento = new RecebimentoCartaoBuilder()
                .comData(adicionarDias(DataUtil.retornarDataAtual(), -365))
                .comValorRecebido(1400.00)
                .construir();

        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimento);

        assertEquals(1, contarRegistros(usuario.getId()));
    }

    @Test
    void naoDeveEnxergarRecebimentoDeOutroUsuario() throws ProjetoException, SQLException {
        Date dataIsolada = adicionarDias(DataUtil.retornarDataAtual(), -730);

        RecebimentoCartao recebimentoA = new RecebimentoCartaoBuilder()
                .comData(dataIsolada)
                .comValorRecebido(500.00)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoA);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        RecebimentoCartao recebimentoB = new RecebimentoCartaoBuilder()
                .comData(dataIsolada)
                .comValorRecebido(250.00)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoB);

        assertEquals(1, contarRegistros(usuario.getId()));
        assertEquals(1, contarRegistros(usuarioB.getId()));
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

    @Test
    void deveConsultarRecebimentosCartaoNoPeriodo() throws ProjetoException {
        Cliente cliente = criarCliente("Cliente Recebimento");
        Date dia1 = adicionarDias(DataUtil.retornarDataAtual(), -400);
        Date dia2 = adicionarDias(DataUtil.retornarDataAtual(), -399);

        inserirVenda(cliente, 100.0, dia1, CARTAO_CREDITO);
        inserirVenda(cliente, 50.0, dia1, CARTAO_DEBITO);

        inserirVenda(cliente, 200.0, dia2, CARTAO_CREDITO);

        RecebimentoCartao recebimentoDia1 = new RecebimentoCartaoBuilder()
                .comData(dia1)
                .comValorRecebido(140.0)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoDia1);

        RecebimentoCartao recebimentoDia2 = new RecebimentoCartaoBuilder()
                .comData(dia2)
                .comValorRecebido(185.0)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoDia2);

        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(dia1, -1));
        busca.setPeriodofinal(adicionarDias(dia2, 1));

        ArrayList<RecebimentoCartaoRelatorioDTO> lista = recebimentosCartaoDAO.consultarRecebimentosCartao(busca);

        assertNotNull(lista);
        assertEquals(2, lista.size());

        RecebimentoCartaoRelatorioDTO primeiro = lista.get(0);
        assertEquals(200.0, primeiro.getTotalDia(), 0.01);
        assertEquals(185.0, primeiro.getValorRecebido(), 0.01);
        assertEquals(15.0, primeiro.getDescontoJuros(), 0.01);
        assertEquals(7.5, primeiro.getPercentualTaxasJuros(), 0.01);

        RecebimentoCartaoRelatorioDTO segundo = lista.get(1);
        assertEquals(150.0, segundo.getTotalDia(), 0.01);
        assertEquals(140.0, segundo.getValorRecebido(), 0.01);
        assertEquals(10.0, segundo.getDescontoJuros(), 0.01);
        assertEquals(6.67, segundo.getPercentualTaxasJuros(), 0.01);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaRecebimentosCartaoNoPeriodo() {
        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(DataUtil.retornarDataAtual(), -90));
        busca.setPeriodofinal(adicionarDias(DataUtil.retornarDataAtual(), -60));

        ArrayList<RecebimentoCartaoRelatorioDTO> lista = recebimentosCartaoDAO.consultarRecebimentosCartao(busca);

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    void naoDeveEnxergarRecebimentosCartaoDeOutroUsuario() throws ProjetoException {
        Cliente clienteA = criarCliente("Cliente UserA");
        Date dataA = adicionarDias(DataUtil.retornarDataAtual(), -500);

        inserirVenda(clienteA, 100.0, dataA, CARTAO_CREDITO);
        RecebimentoCartao recebimentoA = new RecebimentoCartaoBuilder()
                .comData(dataA)
                .comValorRecebido(90.0)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoA);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        Cliente clienteB = criarCliente("Cliente UserB");
        Date dataB = adicionarDias(DataUtil.retornarDataAtual(), -501);

        inserirVenda(clienteB, 200.0, dataB, CARTAO_CREDITO);
        RecebimentoCartao recebimentoB = new RecebimentoCartaoBuilder()
                .comData(dataB)
                .comValorRecebido(180.0)
                .construir();
        recebimentosCartaoDAO.inserirRecebimentoCartao(recebimentoB);

        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(DataUtil.retornarDataAtual(), -510));
        busca.setPeriodofinal(DataUtil.retornarDataAtual());

        ArrayList<RecebimentoCartaoRelatorioDTO> listaB = recebimentosCartaoDAO.consultarRecebimentosCartao(busca);

        assertEquals(1, listaB.size());
        assertEquals(180.0, listaB.get(0).getValorRecebido(), 0.01);
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

    private int contarRegistros(int usuarioId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                PostgresContainerSupport.getContainer().getJdbcUrl(),
                PostgresContainerSupport.getContainer().getUsername(),
                PostgresContainerSupport.getContainer().getPassword());
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM vendas.\"recebimentos_cartão\" WHERE usuario = ?")) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    private Date adicionarDias(Date data, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }
}
