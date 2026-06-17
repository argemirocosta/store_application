package br.com.storeapplication.service;

import br.com.storeapplication.dao.CaixaDiarioDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.CaixaDiario;
import br.com.storeapplication.model.builder.CaixaDiarioBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CaixaDiarioServiceTest {

    @Test
    public void deveDelegarInsercaoAoDAO() throws ProjetoException, NoSuchFieldException, IllegalAccessException {
        CaixaDiarioDAO daoMock = mock(CaixaDiarioDAO.class);

        CaixaDiarioService service = new CaixaDiarioService();
        Field campo = CaixaDiarioService.class.getDeclaredField("caixaDiarioDAO");
        campo.setAccessible(true);
        campo.set(service, daoMock);

        CaixaDiario caixaDiario = new CaixaDiarioBuilder()
                .comData(new Date())
                .comValor(250.00)
                .construir();

        service.inserirCaixaDiario(caixaDiario);

        verify(daoMock).inserirCaixaDiario(caixaDiario);
    }
}
