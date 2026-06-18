package br.com.storeapplication.service;

import br.com.storeapplication.dao.EstoqueDAO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Estoque;
import br.com.storeapplication.model.builder.EstoqueBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EstoqueServiceTest {

    @Test
    public void deveDelegarInsercaoAoDAO() throws ProjetoException, NoSuchFieldException, IllegalAccessException {
        EstoqueDAO daoMock = mock(EstoqueDAO.class);

        EstoqueService service = new EstoqueService();
        Field campo = EstoqueService.class.getDeclaredField("estoqueDAO");
        campo.setAccessible(true);
        campo.set(service, daoMock);

        Estoque estoque = new EstoqueBuilder()
                .comData(new Date())
                .comValor(1000.00)
                .construir();

        service.inserirEstoque(estoque);

        verify(daoMock).inserirEstoque(estoque);
    }
}
