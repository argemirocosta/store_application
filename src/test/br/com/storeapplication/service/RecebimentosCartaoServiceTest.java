package br.com.storeapplication.service;

import br.com.storeapplication.dao.RecebimentosCartaoDAO;
import br.com.storeapplication.dto.DescontoCartaoDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.BuscaRelatorio;
import br.com.storeapplication.model.RecebimentoCartao;
import br.com.storeapplication.model.builder.RecebimentoCartaoBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecebimentosCartaoServiceTest {

    @Test
    void deveDelegarInsercaoAoDAO() throws NoSuchFieldException, IllegalAccessException, ProjetoException {
        RecebimentosCartaoDAO daoMock = mock(RecebimentosCartaoDAO.class);

        RecebimentosCartaoService service = new RecebimentosCartaoService();
        Field campo = RecebimentosCartaoService.class.getDeclaredField("recebimentosCartaoDAO");
        campo.setAccessible(true);
        campo.set(service, daoMock);

        RecebimentoCartao recebimento = new RecebimentoCartaoBuilder()
                .comData(new Date())
                .comValorRecebido(1400.00)
                .construir();

        service.inserirRecebimentoCartao(recebimento);

        verify(daoMock).inserirRecebimentoCartao(recebimento);
    }

    @Test
    void deveDelegarConsultaAoDAO() throws NoSuchFieldException, IllegalAccessException {
        RecebimentosCartaoDAO daoMock = mock(RecebimentosCartaoDAO.class);

        RecebimentosCartaoService service = new RecebimentosCartaoService();
        Field campo = RecebimentosCartaoService.class.getDeclaredField("recebimentosCartaoDAO");
        campo.setAccessible(true);
        campo.set(service, daoMock);

        BuscaRelatorio busca = new BuscaRelatorio();
        busca.setPeriodoinicial(new Date());
        busca.setPeriodofinal(new Date());

        DescontoCartaoDTO dtoEsperado = new DescontoCartaoDTO();
        dtoEsperado.setValorCartaoPeriodo(500.0);
        dtoEsperado.setValorRecebido(470.0);
        dtoEsperado.setValorDescontoCartao(30.0);
        when(daoMock.consultarDescontoCartao(busca)).thenReturn(dtoEsperado);

        DescontoCartaoDTO resultado = service.consultarDescontoCartao(busca);

        verify(daoMock).consultarDescontoCartao(busca);
        assertEquals(500.0, resultado.getValorCartaoPeriodo());
        assertEquals(470.0, resultado.getValorRecebido());
        assertEquals(30.0, resultado.getValorDescontoCartao());
    }
}
