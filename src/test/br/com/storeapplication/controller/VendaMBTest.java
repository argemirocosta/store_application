package br.com.storeapplication.controller;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.integration.FacesContextTestSupport;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.service.RecebimentosCartaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VendaMBTest {

    private final FacesContextTestSupport facesContextTestSupport = new FacesContextTestSupport();
    private VendaMB mb;
    private RecebimentosCartaoService serviceMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        facesContextTestSupport.iniciar();

        serviceMock = mock(RecebimentosCartaoService.class);
        mb = new VendaMB();
        Field campo = VendaMB.class.getDeclaredField("recebimentosCartaoService");
        campo.setAccessible(true);
        campo.set(mb, serviceMock);
    }

    @AfterEach
    void tearDown() {
        facesContextTestSupport.finalizar();
    }

    @Test
    void deveChamarServicoEPopularCamposAoCalcularDescontoCartao() {
        DescontoCartaoDTO dto = new DescontoCartaoDTO();
        dto.setValorCartaoPeriodo(1000.0);
        dto.setValorRecebido(950.0);
        dto.setValorDescontoCartao(50.0);
        when(serviceMock.consultarDescontoCartao(any(BuscaRelatorio.class))).thenReturn(dto);

        mb.calcularDescontoCartao();

        verify(serviceMock).consultarDescontoCartao(any(BuscaRelatorio.class));
        assertEquals(1000.0, mb.getValorCartaoPeriodo());
        assertEquals(950.0, mb.getValorRecebidoCartao());
        assertEquals(50.0, mb.getValorDescontoCartao());
    }
}
