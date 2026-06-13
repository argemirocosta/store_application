package br.com.storeapplication.integration;

import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.shared.Sessao;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsula o ciclo de vida de um MockedStatic<FacesContext> com um Map real
 * representando getSessionMap(), permitindo que util.SessaoUtil funcione fora
 * de uma requisição JSF real.
 *
 * iniciar() deve ser chamado no @BeforeEach e finalizar() no @AfterEach - mocks
 * estáticos do Mockito não são seguros para deixar abertos entre testes.
 */
public class FacesContextTestSupport {

    private final Map<String, Object> sessionMap = new HashMap<>();
    private MockedStatic<FacesContext> facesContextMockedStatic;

    public void iniciar() {
        FacesContext facesContext = Mockito.mock(FacesContext.class);
        ExternalContext externalContext = Mockito.mock(ExternalContext.class);

        Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
        Mockito.when(externalContext.getSessionMap()).thenReturn(sessionMap);

        facesContextMockedStatic = Mockito.mockStatic(FacesContext.class);
        facesContextMockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
    }

    public void finalizar() {
        if (facesContextMockedStatic != null) {
            facesContextMockedStatic.close();
        }
        sessionMap.clear();
    }

    public void loginComoUsuario(Usuario usuario) {
        sessionMap.put(Sessao.USUARIO_SESSAO, usuario);
    }
}
