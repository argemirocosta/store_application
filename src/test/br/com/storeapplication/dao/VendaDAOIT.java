package br.com.storeapplication.dao;

import br.com.storeapplication.dto.VendasComDescontoDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.EstoqueFixture;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VendaDAOIT extends PostgresIntegrationTestBase {

    private static final FormaPagamento DINHEIRO = new FormaPagamentoBuilder().comId(1).comDescricao("DINHEIRO").construir();
    private static final FormaPagamento CARTAO_CREDITO = new FormaPagamentoBuilder().comId(2).comDescricao("CARTÃO CRÉDITO").construir();

    private final VendaDAO vendaDAO = new VendaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    private Usuario usuario;

    @BeforeEach
    void setUp() throws ProjetoException {
        usuario = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuario);
    }

    @Test
    void deveRegistrarVendaEListarParaOCliente() throws ProjetoException {
        Cliente cliente = criarCliente("Cliente Venda");

        Venda venda = new VendaBuilder()
                .comCliente(cliente)
                .comValor(150.0)
                .comQtd(2)
                .comData(DataUtil.retornarDataAtual())
                .comFormaPagamento(DINHEIRO)
                .comDesconto(false)
                .comPercentualDesconto(0.0)
                .construir();

        vendaDAO.inserirVenda(venda);

        List<Venda> vendas = vendaDAO.listarVendas(new VendaBuilder().comCliente(cliente).construir());

        assertEquals(1, vendas.size());
        Venda vendaRegistrada = vendas.get(0);
        assertEquals(150.0, vendaRegistrada.getValor().doubleValue());
        assertEquals(2, vendaRegistrada.getQtd().intValue());
        assertEquals(cliente.getNome(), vendaRegistrada.getCliente().getNome());
        assertEquals("DINHEIRO", vendaRegistrada.getFormaPagamento().getDescricao());
        assertFalse(vendaRegistrada.getDesconto());
    }

    @Test
    void deveCancelarVendaERemoveLaDaListagem() throws ProjetoException {
        Cliente cliente = criarCliente("Cliente Cancelamento");

        Venda venda = new VendaBuilder()
                .comCliente(cliente)
                .comValor(80.0)
                .comQtd(1)
                .comData(DataUtil.retornarDataAtual())
                .comFormaPagamento(DINHEIRO)
                .comDesconto(false)
                .comPercentualDesconto(0.0)
                .construir();

        vendaDAO.inserirVenda(venda);

        Venda filtro = new VendaBuilder().comCliente(cliente).construir();
        List<Venda> vendasAntesDoCancelamento = vendaDAO.listarVendas(filtro);
        assertEquals(1, vendasAntesDoCancelamento.size());

        vendaDAO.cancelarVenda(vendasAntesDoCancelamento.get(0).getId());

        List<Venda> vendasDepoisDoCancelamento = vendaDAO.listarVendas(filtro);
        assertTrue(vendasDepoisDoCancelamento.isEmpty());
    }

    @Test
    void deveSomarVendasComDescontoAgrupadasPorPercentual() throws ProjetoException {
        Cliente cliente = criarCliente("Cliente Desconto");
        Date hoje = DataUtil.retornarDataAtual();

        inserirVenda(cliente, 100.0, hoje, true, 10.0);
        inserirVenda(cliente, 50.0, hoje, true, 10.0);
        inserirVenda(cliente, 80.0, hoje, true, 20.0);
        inserirVenda(cliente, 999.0, hoje, false, 0.0);

        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(adicionarDias(hoje, -1));
        busca.setPeriodofinal(adicionarDias(hoje, 1));

        ArrayList<VendasComDescontoDTO> resultado = vendaDAO.consultarVendasPorPeriodoComDesconto(busca);

        assertEquals(2, resultado.size());
        assertEquals(150.0, somaParaPercentual(resultado, 10.0), 0.01);
        assertEquals(80.0, somaParaPercentual(resultado, 20.0), 0.01);
    }

    @Test
    void deveCalcularEstoqueAplicandoFormulaDeDescontoPorFormaPagamento() throws ProjetoException, SQLException {
        Cliente cliente = criarCliente("Cliente Estoque");
        Date hoje = DataUtil.retornarDataAtual();

        EstoqueFixture.inserirValorEstoque(usuario.getId(), 1000.0);

        // venda em dinheiro (não-crédito), sem desconto: entra com 50% do valor
        inserirVenda(cliente, 100.0, hoje, DINHEIRO, false, 0.0);
        // venda em cartão de crédito, sem desconto: entra com 45% do valor (90% / 2)
        inserirVenda(cliente, 200.0, hoje, CARTAO_CREDITO, false, 0.0);
        // venda com desconto: entra com 100% do valor
        inserirVenda(cliente, 50.0, hoje, DINHEIRO, true, 10.0);

        double esperado = (1000.0 - (100.0 / 2) - ((200.0 / 100 * 90) / 2) - 50.0) * 1.33;

        assertEquals(esperado, vendaDAO.calcularEstoque(), 0.01);
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

    private void inserirVenda(Cliente cliente, double valor, Date data, boolean desconto, double percentualDesconto) throws ProjetoException {
        inserirVenda(cliente, valor, data, DINHEIRO, desconto, percentualDesconto);
    }

    private void inserirVenda(Cliente cliente, double valor, Date data, FormaPagamento formaPagamento, boolean desconto, double percentualDesconto) throws ProjetoException {
        Venda venda = new VendaBuilder()
                .comCliente(cliente)
                .comValor(valor)
                .comQtd(1)
                .comData(data)
                .comFormaPagamento(formaPagamento)
                .comDesconto(desconto)
                .comPercentualDesconto(percentualDesconto)
                .construir();

        vendaDAO.inserirVenda(venda);
    }

    private Date adicionarDias(Date data, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }

    private double somaParaPercentual(List<VendasComDescontoDTO> resultado, double percentualDesconto) {
        return resultado.stream()
                .filter(dto -> Math.abs(dto.getPercentualDesconto() - percentualDesconto) < 0.001)
                .findFirst()
                .map(VendasComDescontoDTO::getSoma)
                .orElseThrow(() -> new IllegalStateException("Nenhum grupo encontrado para percentual " + percentualDesconto));
    }
}
