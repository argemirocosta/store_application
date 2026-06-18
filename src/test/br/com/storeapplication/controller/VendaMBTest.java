package br.com.storeapplication.controller;

import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.dto.RecebimentoCartaoRelatorioDTO;
import br.com.storeapplication.integration.FacesContextTestSupport;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.service.RecebimentosCartaoService;
import br.com.storeapplication.service.VendaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VendaMBTest {

    private final FacesContextTestSupport facesContextTestSupport = new FacesContextTestSupport();
    private VendaMB mb;
    private RecebimentosCartaoService serviceMock;
    private VendaService vendaServiceMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        facesContextTestSupport.iniciar();

        serviceMock = mock(RecebimentosCartaoService.class);
        vendaServiceMock = mock(VendaService.class);
        mb = new VendaMB();

        Field campoRecebimentos = VendaMB.class.getDeclaredField("recebimentosCartaoService");
        campoRecebimentos.setAccessible(true);
        campoRecebimentos.set(mb, serviceMock);

        Field campoVenda = VendaMB.class.getDeclaredField("vendaService");
        campoVenda.setAccessible(true);
        campoVenda.set(mb, vendaServiceMock);
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

    @Test
    void deveChamarServicoEPopularListaAoConsultarRecebimentosCartao() {
        ArrayList<RecebimentoCartaoRelatorioDTO> lista = new ArrayList<>();
        RecebimentoCartaoRelatorioDTO dto = new RecebimentoCartaoRelatorioDTO();
        dto.setTotalDia(200.0);
        dto.setValorRecebido(185.0);
        dto.setDescontoJuros(15.0);
        dto.setPercentualTaxasJuros(7.5);
        lista.add(dto);
        when(serviceMock.consultarRecebimentosCartao(any(BuscaRelatorio.class))).thenReturn(lista);

        mb.consultarRecebimentosCartao();

        verify(serviceMock).consultarRecebimentosCartao(any(BuscaRelatorio.class));
        assertNotNull(mb.getListaRecebimentosCartao());
        assertEquals(1, mb.getListaRecebimentosCartao().size());
        assertEquals(200.0, mb.getListaRecebimentosCartao().get(0).getTotalDia());
    }

    @Test
    void deveChamarServicoAoCalcularMediaDiaria() {
        when(vendaServiceMock.consultarMediaDiariaColecao(any(BuscaRelatorio.class))).thenReturn(150.0);
        when(vendaServiceMock.consultarMediaDiariaPromocao(any(BuscaRelatorio.class))).thenReturn(80.0);

        mb.calcularMediaDiaria();

        assertEquals(150.0, mb.getMediaDiariaColecao());
        assertEquals(80.0, mb.getMediaDiariaPromocao());
    }

    @Test
    void deveChamarServicoAoCalcularEstoque() {
        when(vendaServiceMock.calcularEstoque()).thenReturn(5000.0);

        mb.calcularEstoque();

        assertEquals(5000.0, mb.getValorEstoque());
    }
}
