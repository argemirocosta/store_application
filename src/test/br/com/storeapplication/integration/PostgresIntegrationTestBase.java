package br.com.storeapplication.integration;

import br.com.storeapplication.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Classe-base para testes de integração (*IT) que precisam de um banco Postgres
 * real (via Testcontainers) e de uma sessão JSF simulada (FacesContext mockado).
 *
 * Subclasses ganham automaticamente:
 *  - Container Postgres iniciado e schema "vendas" carregado (uma vez por JVM).
 *  - FacesContext/ExternalContext mockados com um Map de sessão real por teste.
 *
 * Uso: chamar loginComoUsuario(usuario) em cada teste para simular o usuário logado.
 */
public abstract class PostgresIntegrationTestBase {

    private FacesContextTestSupport facesContextTestSupport;

    @BeforeAll
    static void iniciarContainer() {
        PostgresContainerSupport.iniciar();
    }

    @BeforeEach
    void abrirFacesContextMock() {
        facesContextTestSupport = new FacesContextTestSupport();
        facesContextTestSupport.iniciar();
    }

    @AfterEach
    void fecharFacesContextMock() {
        facesContextTestSupport.finalizar();
    }

    protected void loginComoUsuario(Usuario usuario) {
        facesContextTestSupport.loginComoUsuario(usuario);
    }
}
