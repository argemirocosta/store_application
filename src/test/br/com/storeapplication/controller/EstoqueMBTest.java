package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.FacesContextTestSupport;
import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.model.builder.EstoqueBuilder;
import br.com.storeapplication.service.EstoqueService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EstoqueMBTest {

    private final FacesContextTestSupport facesContextTestSupport = new FacesContextTestSupport();
    private EstoqueMB mb;
    private EstoqueService serviceMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        facesContextTestSupport.iniciar();

        serviceMock = mock(EstoqueService.class);
        mb = new EstoqueMB();
        Field campo = EstoqueMB.class.getDeclaredField("estoqueService");
        campo.setAccessible(true);
        campo.set(mb, serviceMock);
    }

    @AfterEach
    void tearDown() {
        facesContextTestSupport.finalizar();
    }

    @Test
    void deveChamarServicoAoInserirEstoque() throws ProjetoException {
        Estoque estoque = new EstoqueBuilder()
                .comData(new Date())
                .comValor(1000.00)
                .construir();
        mb.setEstoque(estoque);

        mb.inserirEstoque();

        verify(serviceMock).inserirEstoque(any(Estoque.class));
    }

    @Test
    void naoDevePropagarExcecaoEmCasoDeFalha() throws ProjetoException {
        doThrow(new ProjetoException(new RuntimeException("erro"))).when(serviceMock).inserirEstoque(any());

        mb.inserirEstoque();

        verify(serviceMock).inserirEstoque(any(Estoque.class));
    }
}
