package br.com.storeapplication.controller;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.integration.FacesContextTestSupport;
import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.model.builder.CaixaDiarioBuilder;
import br.com.storeapplication.service.CaixaDiarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CaixaDiarioMBTest {

    private final FacesContextTestSupport facesContextTestSupport = new FacesContextTestSupport();
    private CaixaDiarioMB mb;
    private CaixaDiarioService serviceMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        facesContextTestSupport.iniciar();

        serviceMock = mock(CaixaDiarioService.class);
        mb = new CaixaDiarioMB();
        Field campo = CaixaDiarioMB.class.getDeclaredField("caixaDiarioService");
        campo.setAccessible(true);
        campo.set(mb, serviceMock);
    }

    @AfterEach
    void tearDown() {
        facesContextTestSupport.finalizar();
    }

    @Test
    void deveAtualizarSaldoAposInsercaoBemSucedida() throws ProjetoException {
        CaixaDiario caixaDiario = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(300.00)
                .construir();
        mb.setCaixaDiario(caixaDiario);
        when(serviceMock.buscarDiferencaCaixa()).thenReturn(500.00);

        mb.inserirCaixaDiario();

        verify(serviceMock).inserirCaixaDiario(any(CaixaDiario.class));
        verify(serviceMock).buscarDiferencaCaixa();
        assertEquals(500.00, mb.getDiferencaCaixa());
    }

    @Test
    void naoDeveAtualizarSaldoEmCasoDeFalha() throws ProjetoException {
        doThrow(new ProjetoException(new RuntimeException("erro"))).when(serviceMock).inserirCaixaDiario(any());

        mb.inserirCaixaDiario();

        verify(serviceMock, never()).buscarDiferencaCaixa();
        assertNull(mb.getDiferencaCaixa());
    }
}
