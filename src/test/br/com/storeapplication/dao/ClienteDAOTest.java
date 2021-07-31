package br.com.storeapplication.dao;

import br.com.storeapplication.builders.ClienteBuilderTest;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Cliente;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ClienteDAOTest {

    private ClienteDAO clienteDaoFake;
    private Cliente cliente1;
    private Cliente cliente2;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void inicializarClasse(){
        clienteDaoFake = mock(ClienteDAO.class);
        cliente1 = ClienteBuilderTest.umClienteTeste1().agora();
        cliente2 = ClienteBuilderTest.umClienteTeste2().agora();
    }

    @Test
    public void listarClientes() {

        List<Cliente> listaClientes = Arrays.asList(cliente1, cliente2);

        when(clienteDaoFake.listarClientes()).thenReturn(listaClientes);

        assertEquals(2, listaClientes.size());
    }

    @Test
    public void buscarClientes() {

        List<Cliente> buscarClientes = Arrays.asList(cliente1);

        when(clienteDaoFake.buscarClientePorNome("1")).thenReturn(buscarClientes);

        assertEquals(1, buscarClientes.size());
    }

    @Test
    public void inserirCliente() throws ProjetoException {
        clienteDaoFake.inserirCliente(cliente1);

        verify(clienteDaoFake, times(1)).inserirCliente(cliente1);
    }

    @Test
    public void alterarCliente() throws ProjetoException {
        clienteDaoFake.alterarCliente(cliente1);

        verify(clienteDaoFake, times(1)).alterarCliente(cliente1);
    }

    @Test
    public void deletarCliente() throws ProjetoException {
        clienteDaoFake.deletarCliente(cliente1);

        verify(clienteDaoFake, times(1)).deletarCliente(cliente1);
    }

}
