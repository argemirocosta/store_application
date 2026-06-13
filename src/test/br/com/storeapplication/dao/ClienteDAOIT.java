package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.PostgresIntegrationTestBase;
import br.com.storeapplication.integration.UsuarioFixture;
import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.builder.ClienteBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteDAOIT extends PostgresIntegrationTestBase {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    private Usuario usuario;

    @BeforeEach
    void setUp() throws ProjetoException {
        usuario = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuario);
    }

    @Test
    void deveCadastrarClienteComCpfETelefoneENomeEmUpperCase() throws ProjetoException {
        Cliente cliente = new ClienteBuilder()
                .comNome("maria da silva")
                .comCpf("256.235.615-27")
                .comTelefone1("11999998888")
                .construir();

        clienteDAO.inserirCliente(cliente);

        List<Cliente> clientes = clienteDAO.listarClientes();

        assertEquals(1, clientes.size());
        Cliente clientePersistido = clientes.get(0);
        assertEquals("MARIA DA SILVA", clientePersistido.getNome());
        assertEquals("25623561527", clientePersistido.getCpf());
    }

    @Test
    void deveDetectarTelefoneJaCadastradoParaOMesmoUsuario() throws ProjetoException {
        Cliente cliente = new ClienteBuilder()
                .comNome("Cliente Telefone")
                .comTelefone1("11999998888")
                .construir();

        clienteDAO.inserirCliente(cliente);

        assertTrue(clienteDAO.verificarClienteCadastrado("11999998888"));
        assertFalse(clienteDAO.verificarClienteCadastrado("11900000000"));
    }

    @Test
    void naoDeveEnxergarClientesDeOutroUsuario() throws ProjetoException {
        Cliente clienteDoUsuarioA = new ClienteBuilder()
                .comNome("Cliente A")
                .comTelefone1("11911111111")
                .construir();
        clienteDAO.inserirCliente(clienteDoUsuarioA);

        Usuario usuarioB = UsuarioFixture.criarUsuario();
        loginComoUsuario(usuarioB);

        Cliente clienteDoUsuarioB = new ClienteBuilder()
                .comNome("Cliente B")
                .comTelefone1("11922222222")
                .construir();
        clienteDAO.inserirCliente(clienteDoUsuarioB);

        List<Cliente> clientesDoUsuarioB = clienteDAO.listarClientes();

        assertEquals(1, clientesDoUsuarioB.size());
        assertEquals("CLIENTE B", clientesDoUsuarioB.get(0).getNome());
    }
}
