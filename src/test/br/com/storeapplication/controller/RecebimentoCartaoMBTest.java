package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.FacesContextTestSupport;
import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.model.builder.RecebimentoCartaoBuilder;
import br.com.storeapplication.service.RecebimentosCartaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RecebimentoCartaoMBTest {

    private final FacesContextTestSupport facesContextTestSupport = new FacesContextTestSupport();
    private RecebimentoCartaoMB mb;
    private RecebimentosCartaoService serviceMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        facesContextTestSupport.iniciar();

        serviceMock = mock(RecebimentosCartaoService.class);
        mb = new RecebimentoCartaoMB();
        Field campo = RecebimentoCartaoMB.class.getDeclaredField("recebimentosCartaoService");
        campo.setAccessible(true);
        campo.set(mb, serviceMock);
    }

    @AfterEach
    void tearDown() {
        facesContextTestSupport.finalizar();
    }

    @Test
    void deveChamarServicoAoInserirRecebimentoCartao() throws ProjetoException {
        RecebimentoCartao recebimento = new RecebimentoCartaoBuilder()
                .comData(new Date())
                .comValorRecebido(1400.00)
                .construir();
        mb.setRecebimentoCartao(recebimento);

        mb.inserirRecebimentoCartao();

        verify(serviceMock).inserirRecebimentoCartao(any(RecebimentoCartao.class));
    }

    @Test
    void naoDevePropagarExcecaoEmCasoDeFalha() throws ProjetoException {
        doThrow(new ProjetoException(new RuntimeException("erro"))).when(serviceMock).inserirRecebimentoCartao(any());

        mb.inserirRecebimentoCartao();

        verify(serviceMock).inserirRecebimentoCartao(any(RecebimentoCartao.class));
    }
}
